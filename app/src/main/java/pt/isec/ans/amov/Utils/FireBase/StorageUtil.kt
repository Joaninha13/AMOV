package pt.isec.ans.amov.Utils.FireBase

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import pt.isec.ans.amov.dataStructures.Attraction
import pt.isec.ans.amov.dataStructures.AttractionDetails
import pt.isec.ans.amov.dataStructures.Category
import pt.isec.ans.amov.dataStructures.CategoryDetails
import pt.isec.ans.amov.dataStructures.Location
import pt.isec.ans.amov.dataStructures.LocationDetails
import pt.isec.ans.amov.dataStructures.Review
import pt.isec.ans.amov.dataStructures.ReviewsDetails
import java.util.UUID
import java.util.concurrent.CountDownLatch
import kotlin.math.*

class StorageUtil {
    companion object {

        //ver depois de testar o createUserWithEmail
        fun addUser(name: String, image: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val User = hashMapOf(
                "Name" to name,
                "Photo" to image,
            )

            if (AuthUtil.currentUser != null) {
                db.collection("Users").document(AuthUtil.currentUser!!.uid).set(User)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }

        fun getUserDetails(onResult: (String, String) -> Unit) {

            val db = Firebase.firestore

            if (AuthUtil.currentUser != null) {
                db.collection("Users").document(AuthUtil.currentUser!!.uid).get()
                    .addOnSuccessListener { result ->
                        val name = result.getString("Name")
                        val image = result.getString("Photo")
                        onResult(name!!, image!!)
                    }
            }
        }

        fun getNumCategoriesByUser(onResult: (Int) -> Unit) {

            val db = Firebase.firestore

            if (AuthUtil.currentUser != null) {
                db.collection("Category")
                    .whereEqualTo("User", db.document("Users/${AuthUtil.currentUser!!.uid}"))
                    .get()
                    .addOnSuccessListener { result ->
                        onResult(result.size())
                    }
            }
        }

        fun getNumLocationsByUser(onResult: (Int) -> Unit) {

            val db = Firebase.firestore

            if (AuthUtil.currentUser != null) {
                db.collection("Location")
                    .whereEqualTo("User", db.document("Users/${AuthUtil.currentUser!!.uid}"))
                    .get()
                    .addOnSuccessListener { result ->
                        onResult(result.size())
                    }
            }
        }

        fun getNumAttractionsByUser(onResult: (Int) -> Unit) {

            val db = Firebase.firestore

            if (AuthUtil.currentUser != null) {
                db.collection("Attractions")
                    .whereEqualTo("User", db.document("Users/${AuthUtil.currentUser!!.uid}"))
                    .get()
                    .addOnSuccessListener { result ->
                        onResult(result.size())
                    }
            }
        }

        fun uploadImageToFirebaseStorage(imageUri: Uri, onSuccess: (String) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference
            val imagesRef: StorageReference = storageRef.child("images/${UUID.randomUUID()}")

            imagesRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        onSuccess(uri.toString())
                    }
                }
        }

        fun uploadImagesToFirebaseStorage(imageUris: List<Uri>, onSuccess: (List<String>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference

            val imagesList: MutableList<String> = mutableListOf()

            var completedCount = 0

            for (imageUri in imageUris) {
                val imagesRef: StorageReference = storageRef.child("images/${UUID.randomUUID()}")
                imagesRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        imagesRef.downloadUrl.addOnSuccessListener { uri ->
                            imagesList.add(uri.toString())
                            completedCount++

                            if (completedCount == imageUris.size)
                                onSuccess(imagesList)
                        }
                    }
            }

        }

        //Categorys
        fun addCategory(name: String, desc: String, logo: String, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore

            val category = hashMapOf(
                "Name" to name,
                "Description" to desc,
                "Approved" to 0,
                "Logo" to logo,
                "User" to db.document("Users/${AuthUtil.currentUser!!.uid}")
            )

            val doc = db.collection("Category").document(name)
            doc.get()
                .addOnSuccessListener { result ->
                    if (!result.exists()) {
                        doc.set(category)
                            .addOnSuccessListener {
                                onResult(null)
                            }
                            .addOnFailureListener { e ->
                                onResult(e)
                            }
                    } else {
                        val error = Throwable("Category with name '$name' already exists.")
                        onResult(error)
                    }
                }
                .addOnFailureListener { e ->
                    onResult(e)
                }
        }

        fun getAllDetailsFromCategoryByUser(onResult: (List<CategoryDetails>, Throwable?) -> Unit) {
            val db = Firebase.firestore
            val currentUserUid = AuthUtil.currentUser?.uid

            if (currentUserUid != null) {
                db.collection("Category")
                    .whereEqualTo("User", db.document("Users/$currentUserUid"))
                    .get()
                    .addOnSuccessListener { result ->
                        val categoryDetailsList = mutableListOf<CategoryDetails>()

                        val attractionsCountPromises = result.documents.map { categoryDocument ->
                            val categoryId = categoryDocument.id
                            db.collection("Attractions")
                                .whereEqualTo("Category", db.document("Category/$categoryId"))
                                .get()
                                .addOnSuccessListener { attractionsResult ->
                                    val numAttractions = attractionsResult.size()
                                    val numApproved =
                                        categoryDocument.getLong("Approved")?.toInt() ?: 0
                                    val name = categoryDocument.getString("Name") ?: ""
                                    val description =
                                        categoryDocument.getString("Description") ?: ""
                                    val logoUrl = categoryDocument.getString("Logo") ?: ""

                                    val categoryDetails = CategoryDetails(
                                        name,
                                        numAttractions,
                                        numApproved,
                                        description,
                                        logoUrl
                                    )
                                    categoryDetailsList.add(categoryDetails)

                                    if (categoryDetailsList.size == result.size()) {
                                        onResult(categoryDetailsList, null)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    onResult(emptyList(), e)
                                }
                        }
                        Tasks.whenAllComplete(attractionsCountPromises)
                    }
                    .addOnFailureListener { e ->
                        onResult(emptyList(), e)
                    }
            }
        }

        fun getAllCategorysDocumentsNames(onResult: (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Category")

            doc.get().addOnSuccessListener { result ->
                val names = ArrayList<String>()
                for (document in result) {
                    names.add(document.id)
                }
                onResult(names)
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }

        fun getAllFromOneCategory(name: String, onResult: (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Category").document(name)

            doc.get().addOnSuccessListener { result ->
                if (result.exists())
                    onResult(result.data!!.entries.sortedBy { it.key }
                        .map { it.value?.toString() ?: "" } ?: emptyList())
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }

        fun getCategoryDetails(userGeo: GeoPoint, name: String, onResult: (Category?) -> Unit) {

            val db = Firebase.firestore

            val doc = db.collection("Category").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val fetchedName = document["Name"] as? String ?: "Unknown"
                    val description =
                        document["Description"] as? String ?: "No description available"
                    val logoUrl = document["Logo"] as? String
                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"
                    val numApproved = document["Approved"] as? Number ?: 0

                    val userRef = document["User"] as? String ?: "Unknown"


                    getAttractionsByCategory(category = name, userGeo = userGeo) { linkedAttractions ->
                        val category = Category(
                            name = fetchedName,
                            description = description,
                            logoUrl = logoUrl,
                            userRef = userRef,
                            linkedAttractions = linkedAttractions,
                            numApproved = numApproved.toInt(),
                            numAttractions = linkedAttractions.size,
                        )

                        onResult(category)
                    }
                } else {
                    onResult(null)
                }
            }.addOnFailureListener {
                onResult(null)
            }
        }

        fun getCategoryDetails(name: String, onResult: (Category?) -> Unit) {

            val db = Firebase.firestore

            // Attempt to fetch the single location document based on country and region
            val doc = db.collection("Category").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val fetchedName = document["Name"] as? String ?: "Unknown"
                    val description =
                        document["Description"] as? String ?: "No description available"
                    val logoUrl = document["Logo"] as? String
                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"
                    val numApproved = document["Approved"] as? Number ?: 0

                    val userRef = document["User"] as? DocumentReference

                    if (userRef == null){
                        val category = Category(
                            name = fetchedName,
                            description = description,
                            logoUrl = logoUrl,
                            userRef = "Unknown",
                            linkedAttractions = emptyList(),
                            numApproved = numApproved.toInt(),
                            numAttractions = 0
                        )
                        onResult(category)
                    }

                    userRef?.get()?.addOnSuccessListener { userDocument ->

                       val userName = userDocument.getString("Name") ?: "Unknown"

                        val category = Category(
                            name = fetchedName,
                            description = description,
                            logoUrl = logoUrl,
                            userRef = userName,
                            linkedAttractions = emptyList(),
                            numApproved = numApproved.toInt(),
                            numAttractions = 0
                        )

                        onResult(category)
                    }
                } else {
                    onResult(null)
                }
            }.addOnFailureListener {
                onResult(null)
            }
        }

        fun updateCategory(categoryName: String, name: String, desc: String, logo: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Category").document(categoryName)
            val userId = AuthUtil.currentUser!!.uid

            // Verifica se o user atual é o mesmo que criou a localização
            db.collection("Category").document(categoryName).get()
                .addOnSuccessListener { documentSnapshot ->
                    val creatorUserId = documentSnapshot.getDocumentReference("User")?.id

                    Log.d("creatorUserId ->>>>>>>>", creatorUserId.toString())

                    if (creatorUserId == userId) {
                        // A categoria pertence ao user, pode editar
                        if (categoryName != name) {
                            addCategory(name, desc, logo) {}
                            deleteCategory(categoryName) {}
                        } else {
                            db.runTransaction { transaction ->
                                val doc = transaction.get(v)
                                if (doc.exists()) {
                                    transaction.update(v, "Description", desc)
                                    transaction.update(v, "Logo", logo)
                                    transaction.update(v, "Approved", 0)
                                    null
                                } else {
                                    throw FirebaseFirestoreException(
                                        "Doesn't exist",
                                        FirebaseFirestoreException.Code.UNAVAILABLE
                                    )
                                }
                            }.addOnCompleteListener { result ->
                                onResult(result.exception)
                            }
                        }
                    } else {
                        // A categoria nao pertence ao User nao pode editar
                        onResult(SecurityException("User does not have permission to edit this category"))
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(exception)
                }
        }

        fun updateApprovedCategory(name: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Category").document(name)

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val approved = (doc.getLong("Approved") ?: 0) + 1
                    transaction.update(v, "Approved", approved)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener { result ->
                onResult(result.exception)
            }
        }
        fun deleteCategory(name: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val docAtt =
                db.collection("Attractions").whereEqualTo("Category", db.document("Category/$name"))

            docAtt.get().addOnSuccessListener { result ->
                if (result.isEmpty) {
                    val doc = db.collection("Category").document(name)
                    doc.get().addOnSuccessListener { results ->
                        if (results.exists()) {
                            doc.delete()
                        }
                    }
                }
            }
        }


        //Locations
        fun addLocation(country: String, region: String, desc: String, coordinates: GeoPoint, image: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val location = hashMapOf(
                "Country" to country,
                "Region" to region,
                "Description" to desc,
                "Coordinates" to coordinates,
                "Image" to image,
                "Approved" to 0,
                "User" to db.document("Users/${AuthUtil.currentUser!!.uid}")
            )

            val doc = db.collection("Location").document("${country}_${region}")

            doc.get().addOnSuccessListener { result ->
                if (!result.exists())
                    doc.set(location)
            }
        }
        fun getAllLocationsDocumentsNames(onResult: (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Location")

            doc.get().addOnSuccessListener { result ->
                val names = ArrayList<String>()
                for (document in result) {
                    names.add(document.id)
                }
                onResult(names)
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun getAllDetailsFromLocationByUser(onResult: (List<LocationDetails>, Throwable?) -> Unit) {
            val db = Firebase.firestore
            val currentUserUid = AuthUtil.currentUser?.uid

            if (currentUserUid != null) {
                db.collection("Location")
                    .whereEqualTo("User", db.document("Users/$currentUserUid"))
                    .get()
                    .addOnSuccessListener { result ->
                        val locationDetailsList = mutableListOf<LocationDetails>()

                        val attractionsCountPromises = result.documents.map { locationDocument ->
                            val locationId = locationDocument.id
                            db.collection("Attractions")
                                .whereEqualTo("Location", db.document("Location/$locationId"))
                                .get()
                                .addOnSuccessListener { attractionsResult ->
                                    val numAttractions = attractionsResult.size()
                                    val numApproved =
                                        locationDocument.getLong("Approved")?.toInt() ?: 0
                                    val country = locationDocument.getString("Country") ?: ""
                                    val region = locationDocument.getString("Region") ?: ""
                                    val imageUrl =
                                        locationDocument.getString("Image") ?: ""

                                    val locationDetails = LocationDetails(
                                        region,
                                        country,
                                        numApproved,
                                        numAttractions,
                                        imageUrl,
                                    )
                                    locationDetailsList.add(locationDetails)

                                    if (locationDetailsList.size == result.size()) {
                                        onResult(locationDetailsList, null)
                                    }
                                }
                                .addOnFailureListener { e ->
                                    onResult(emptyList(), e)
                                }
                        }

                        Tasks.whenAllComplete(attractionsCountPromises)
                    }
                    .addOnFailureListener { e ->
                        onResult(emptyList(), e)
                    }
            }

        }
        fun getAllLocationsDocumentsCoordinates(onResult: (List<GeoPoint>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Location")

            doc.get().addOnSuccessListener { result ->
                val coordinates = ArrayList<GeoPoint>()
                for (document in result) {
                    val geoPoint = document["Coordinates"] as? GeoPoint

                    if (geoPoint != null) {
                        coordinates.add(GeoPoint(geoPoint.latitude, geoPoint.longitude))
                    }
                }
                onResult(coordinates)
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun getAllFromOneLocation(name: String, onResult: (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Location").document(name)

            doc.get().addOnSuccessListener { result ->
                if (result.exists())
                    onResult(result.data!!.entries?.sortedBy { it.key }
                        ?.map { it.value?.toString() ?: "" } ?: emptyList())
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun getLocationDetails(userGeo: GeoPoint, name: String, onResult: (Location?) -> Unit) {

            val db = Firebase.firestore

            // Attempt to fetch the single location document based on country and region
            val doc = db.collection("Location").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val fetchedCountry = document["Country"] as? String ?: "Unknown"
                    val fetchedRegion = document["Region"] as? String ?: "Unknown"

                    val description = document["Description"] as? String ?: "No description available"
                    val geoPoint = document["Coordinates"] as? GeoPoint ?: GeoPoint(0.0, 0.0)
                    val imageUrl = document["Image"] as? String ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"
                    val userRef = document["User"] as? DocumentReference
                    val numApproved = document["Approved"] as? Number ?: 0
                    var userName = "Unknown"

                    userRef?.get()?.addOnSuccessListener { userDocument ->
                        userName = userDocument.getString("Name") ?: "Unknown"

                        val distanceInKmFromCurrent = calculateDistance(userGeo, geoPoint)
                        getAttractionsByLocation(location = name, userGeo = userGeo) { linkedAttractions ->
                            val locationDetails = Location(
                                linkedAttractions = linkedAttractions,
                                country = fetchedCountry,
                                region = fetchedRegion,
                                numAttractions = linkedAttractions.size,
                                distanceInKmFromCurrent = distanceInKmFromCurrent,
                                description = description,
                                coordinates = geoPoint,
                                imageUrl = imageUrl,
                                userRef = userName,
                                numApproved = numApproved.toInt()
                            )

                            onResult(locationDetails)
                        }
                    }?.addOnFailureListener { e ->
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
            }.addOnFailureListener {
                onResult(null)
            }
        }

        //esta funcao serve para ir buscar os details da locations dado um GeoPoint -> overload
        fun getLocationDetails(userGeo: GeoPoint, locationGeoPoint: GeoPoint, onResult: (Location?) -> Unit) {
            fun calculateDistance(startGeo: GeoPoint, endGeo: GeoPoint): Float {
                val earthRadius = 6371 // Radius of the earth in km
                val latDistance = Math.toRadians(endGeo.latitude - startGeo.latitude)
                val lonDistance = Math.toRadians(endGeo.longitude - startGeo.longitude)
                val a = sin(latDistance / 2) * sin(latDistance / 2) +
                        cos(Math.toRadians(startGeo.latitude)) * cos(Math.toRadians(endGeo.latitude)) *
                        sin(lonDistance / 2) * sin(lonDistance / 2)
                val c = 2 * atan2(sqrt(a), sqrt(1 - a))
                return (earthRadius * c).toFloat() // convert the distance from double to float
            }

            val db = Firebase.firestore
            val docRef = db.collection("Location")

            docRef
                .whereEqualTo("Coordinates", locationGeoPoint)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val country = result.documents[0].getString("Country") ?: "Unknown"
                        val region = result.documents[0].getString("Region") ?: "Unknown"
                        val numAttractions = result.documents[0].getLong("NumAttractions")?.toInt() ?: 0
                        val description = result.documents[0].getString("Description") ?: "Unknown"
                        val coordinates = result.documents[0].getGeoPoint("Coordinates") ?: GeoPoint(0.0, 0.0)
                        val imageUrl = result.documents[0].getString("Image") ?: ""

                        val location = Location(
                            country = country,
                            region = region,
                            numAttractions = numAttractions,
                            distanceInKmFromCurrent = calculateDistance(locationGeoPoint, userGeo),
                            description = description,
                            coordinates = coordinates,
                            imageUrl = imageUrl
                        )

                        onResult(location)
                    } else {
                        onResult(null)
                    }
                }
                .addOnFailureListener {
                    onResult(null)
                }
        }
        fun updateLocation(locationName: String,country: String, region : String, desc: String, coordinates: GeoPoint, image : String, onResult : (Throwable?) -> Unit) {


            val db = Firebase.firestore
            val v = db.collection("Location").document("${country}_${region}")
            val userId = AuthUtil.currentUser!!.uid

            // Verifica se o user atual é o mesmo que criou a localização
            db.collection("Location").document(locationName).get()
                .addOnSuccessListener { documentSnapshot ->
                    val creatorUserId = documentSnapshot.getDocumentReference("User")?.id

                    Log.d("creatorUserId ->>>>>>>>", creatorUserId.toString())

                    if (creatorUserId == userId) {
                        // A localização pertence ao User, pode editar
                        if (locationName != "${country}_${region}") {
                            addLocation(country, region, desc, coordinates, image) {}
                            deleteLocation(locationName) {}
                        } else {
                            db.runTransaction { transaction ->
                                val doc = transaction.get(v)
                                if (doc.exists()) {
                                    transaction.update(v, "Description", desc)
                                    transaction.update(v, "Coordinates", coordinates)
                                    transaction.update(v, "Image", image)
                                    transaction.update(v, "Approved", 0)
                                    null
                                } else {
                                    throw FirebaseFirestoreException(
                                        "Doesn't exist",
                                        FirebaseFirestoreException.Code.UNAVAILABLE
                                    )
                                }
                            }.addOnCompleteListener { result ->
                                onResult(result.exception)
                            }
                        }
                    } else {
                        // A localização nao pertence ao User, nao pode editar
                        onResult(SecurityException("User does not have permission to edit this location"))
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(exception)
                }
        }
        fun updateApprovedLocation(LocationName: String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Location").document(LocationName)

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val approved = (doc.getLong("Approved") ?: 0) + 1
                    transaction.update(v, "Approved", approved)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener{result ->
                onResult(result.exception)
            }
        }
        fun deleteLocation(nameToDelete: String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val docAtt = db.collection("Attractions").whereEqualTo("Location", db.document("Location/$nameToDelete"))

            docAtt.get().addOnSuccessListener { result ->
                if (result.isEmpty) {
                    val doc = db.collection("Location").document(nameToDelete)
                    doc.get().addOnSuccessListener { results ->
                        if (results.exists())
                            doc.delete()
                    }
                }
            }
        }


        //Attractions
        fun addAttraction(name: String, desc : String, coordinates: GeoPoint, category : String, Location : String, images : List<String>, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val attraction = hashMapOf(
                "Name" to name,
                "Description" to desc,
                "Coordinates" to coordinates,
                "Approved" to 0,
                "DeleteApproved" to 0,
                "ToDelete" to false,
                "Category" to db.document("Category/$category"),
                "Location" to db.document("Location/$Location"),
                "User" to db.document("Users/${AuthUtil.currentUser!!.uid}"),
                "Images" to images
            )

            val doc = db.collection("Attractions").document(name)

            doc.get().addOnSuccessListener { result ->
                if (!result.exists())
                    doc.set(attraction)

            }
        }
        fun getAllAttractionsDocumentsNames(onResult : (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Attractions")

            doc.get().addOnSuccessListener { result ->
                val names = ArrayList<String>()
                for (document in result) {
                    names.add(document.id)
                }
                onResult(names)
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun getAttractionDetails(name: String, onResult: (List<String>) -> Unit) {

            val db = Firebase.firestore

            val doc = db.collection("Attractions").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val Name = document["Name"] as? String ?: "Unknown"
                    val Description = document["Description"] as? String ?: "No description available"
                    val Category = (document["Category"] as? DocumentReference)?.id ?: "Unknown"
                    val Location = (document["Location"] as? DocumentReference)?.id ?: "Unknown"
                    val images = document["Images"] as? List<String> ?: emptyList()
                    val coordinates = document["Coordinates"] as? GeoPoint ?: GeoPoint(0.0, 0.0)

                    val list = listOf(Name, Description, Category, Location, images.toString(), coordinates.toString())

                    onResult(list)
                } else {
                    onResult(emptyList())
                }
            }.addOnFailureListener {
                onResult(emptyList())
            }
        }

        fun updateAttraction(attractionName: String,name: String, desc : String, coordinates: GeoPoint, category : String, Location : String, images : List<String>, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Attractions").document(attractionName)
            val userId = AuthUtil.currentUser!!.uid

            // Ve se a attraction pertence ao user atual
            db.collection("Attractions").document(attractionName).get()
                .addOnSuccessListener { documentSnapshot ->
                    val creatorUserId = documentSnapshot.getDocumentReference("User")?.id

                    Log.d("creatorUserId ->>>>>>>>", creatorUserId.toString())

                    if (creatorUserId == userId) {
                        // A attraction pertence ao user atual, pode editar
                        if (attractionName != name) {
                            addAttraction(name, desc, coordinates, category, Location, images) {}
                            deleteAttractionFromEdit(attractionName, 3) {}
                        } else {
                            db.runTransaction { transaction ->
                                val doc = transaction.get(v)
                                if (doc.exists()) {
                                    transaction.update(v, "Description", desc)
                                    transaction.update(v, "Coordinates", coordinates)
                                    transaction.update(v, "Category", db.document("Categories/$category"))
                                    transaction.update(v, "Location", db.document("Location/$Location"))
                                    transaction.update(v, "Images", images)
                                    transaction.update(v, "Approved", 0)
                                    null
                                } else {
                                    throw FirebaseFirestoreException(
                                        "Doesn't exist",
                                        FirebaseFirestoreException.Code.UNAVAILABLE
                                    )
                                }
                            }.addOnCompleteListener { result ->
                                onResult(result.exception)
                            }
                        }
                    } else {
                        // A attraction nao pertence ao user atual, nao pode editar
                        onResult(SecurityException("User does not have permission to edit this attraction"))
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(exception)
                }
        }

        fun getAllAttractionsDocumentsCoordinates(onResult: (List<GeoPoint>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Attractions")

            doc.get().addOnSuccessListener { result ->
                val coordinates = ArrayList<GeoPoint>()
                for (document in result) {
                    val geoPoint = document["Coordinates"] as? GeoPoint

                    if (geoPoint != null) {
                        coordinates.add(GeoPoint(geoPoint.latitude, geoPoint.longitude))
                    }
                }
                onResult(coordinates)
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun getAttractionCategory(attraction: GeoPoint, onResult: (String?) -> Unit) {
            val db = Firebase.firestore
            val docRef = db.collection("Attractions")

            docRef
                .whereEqualTo("Coordinates", attraction)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val categoryRef = result.documents[0].getDocumentReference("Category")

                        categoryRef?.get()
                            ?.addOnSuccessListener { categoryDoc ->
                                if (categoryDoc != null) {
                                    val category = categoryDoc.getString("Name")
                                    onResult(category)
                                } else {
                                    onResult(null)
                                }
                            }
                            ?.addOnFailureListener {
                                onResult(null)
                            }
                    } else {
                        onResult(null)
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(null)
                }
        }

        fun getAttractionDetails(attractionGeoPoint: GeoPoint, onResult: (Attraction?) -> Unit) {
            val db = Firebase.firestore
            val docRef = db.collection("Attractions")

            docRef
                .whereEqualTo("Coordinates", attractionGeoPoint)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val name = result.documents[0].getString("Name") ?: "Unknown"
                        val description = result.documents[0].getString("Description") ?: "Unknown"
                        val categoryRef = result.documents[0].get("Category") as? DocumentReference
                        val imageUrlList = result.documents[0].get("Images") as? List<String> ?: emptyList()

                        categoryRef?.get()?.addOnSuccessListener { categorySnapshot ->
                            val categoryName = categorySnapshot.getString("Name") ?: "Unknown"

                            val attraction = Attraction(
                                name = name,
                                coordinates = attractionGeoPoint,
                                description = description,
                                category = categoryName,
                                imageUrlList = imageUrlList
                            )

                            onResult(attraction)
                        }?.addOnFailureListener {
                            onResult(null)
                        }
                    } else {
                        onResult(null)
                    }
                }
                .addOnFailureListener {
                    onResult(null)
                }
        }

        fun getAllDetailsFromAttractionByUser(onResult: (List<AttractionDetails>, Throwable?) -> Unit) {
            val db = Firebase.firestore
            val userUid = AuthUtil.currentUser?.uid

            if (userUid == null) {
                // Usuário não autenticado, retornar erro
                onResult(emptyList(), Throwable("User not authenticated"))
                return
            }

            db.collection("Attractions")
                .whereEqualTo("User", db.document("Users/$userUid"))
                .get()
                .addOnSuccessListener { result ->
                    val attractionDetailsList = mutableListOf<AttractionDetails>()

                    for (document in result) {
                        val name = document.getString("Name") ?: ""
                        val attractionId = document.id
                        val approved = document.getLong("Approved") ?: 0
                        val approvedToDelete = document.getLong("DeleteApproved") ?: 0
                        val toDelete = document.getBoolean("ToDelete") ?: false
                        val image = document.get("Images") as? List<String> ?: emptyList()

                        Log.d("IMAGEEE ->>>>>>>", image.toString())

                        // Calcular a média das classificações para a atração atual
                        db.collection("Reviews")
                            .whereEqualTo("Attraction", db.document("Attractions/$attractionId"))
                            .get()
                            .addOnSuccessListener { reviewsResult ->
                                val numReviews = reviewsResult.size()

                                var totalRating = 0.0

                                for (reviewDocument in reviewsResult) {
                                    val rating = reviewDocument.get("Rating") as? Number ?: 0
                                    totalRating += rating.toDouble()
                                }

                                val averageRating = if (numReviews > 0) totalRating / numReviews else 0.0

                                val attractionDetails = AttractionDetails(name, "%.1f".format(averageRating), numReviews, approved.toInt(), approvedToDelete.toInt(), toDelete, image)
                                attractionDetailsList.add(attractionDetails)

                                if (attractionDetailsList.size == result.size()) {
                                    onResult(attractionDetailsList, null)
                                }
                            }
                            .addOnFailureListener { reviewsException ->
                                onResult(emptyList(), reviewsException)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(emptyList(), exception)
                }
        }
        fun updateApprovedAttraction( name: String,onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Attractions").document(name)

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val approved = (doc.getLong("Approved") ?: 0) + 1
                    transaction.update(v, "Approved", approved)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener{result ->
                onResult(result.exception)
            }
        }

        fun updateDeleteApprovedAttraction(name: String, onResult: (Throwable?) -> Unit){

                val db = Firebase.firestore
                val v = db.collection("Attractions").document(name)

                db.runTransaction { transaction ->
                    val doc = transaction.get(v)
                    if (doc.exists()) {
                        val deleteapproved = (doc.getLong("DeleteApproved") ?: 0) + 1
                        transaction.update(v, "DeleteApproved", deleteapproved)
                        null
                    } else
                        throw FirebaseFirestoreException(
                            "Doesn't exist",
                            FirebaseFirestoreException.Code.UNAVAILABLE
                        )
                }.addOnCompleteListener{result ->
                    onResult(result.exception)
                }
        }

        fun switchToDeleteAttraction(name: String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Attractions").document(name)

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val todelete = doc.getBoolean("ToDelete") ?: false
                    transaction.update(v, "ToDelete", !todelete)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }.addOnCompleteListener{result ->
                onResult(result.exception)
            }
        }

        fun getToDeleteAttraction(name: String, onResult : (Boolean) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Attractions").document(name)

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val todelete = doc.getBoolean("ToDelete") ?: false

                    onResult(todelete)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }
        }

        fun deleteAttraction(name: String, onResult : (Throwable?) -> Unit) {

            //As Attractions podem ser eliminados pelos seus autores, mas depois de possuírem votações
            //só o poderão ser com a aprovação de remoção por 3 utilizadores

            val db = Firebase.firestore

            db.collection("Attractions").document(name).get().addOnSuccessListener { result ->
                    if (result.exists()) {
                        val approved = result.getLong("Approved")
                        val isdelete = result.getLong("DeleteApproved")
                        if (approved!! < 3 || isdelete!! >= 3) {
                            val doc = db.collection("Attractions").document(name)
                            doc.get().addOnSuccessListener { results ->
                                if (results.exists())
                                    doc.delete()
                            }
                        }
                        else {
                            val v = db.collection("Attractions").document(name)
                            db.runTransaction { transaction ->
                                val doc = transaction.get(v)
                                if (doc.exists()) {
                                    val deleteapproved = (doc.getLong("DeleteApproved") ?: 0) + 1
                                    transaction.update(v, "DeleteApproved", deleteapproved)
                                    null
                                } else
                                    throw FirebaseFirestoreException(
                                        "Doesn't exist",
                                        FirebaseFirestoreException.Code.UNAVAILABLE
                                    )
                            }.addOnCompleteListener { result ->
                                onResult(result.exception)
                            }

                        }
                    }
                }

        }

        fun deleteAttractionFromEdit(name: String, delete: Number,onResult : (Throwable?) -> Unit) {

            //As Attractions podem ser eliminados pelos seus autores, mas depois de possuírem votações
            //só o poderão ser com a aprovação de remoção por 3 utilizadores

            val db = Firebase.firestore

            db.collection("Attractions").document(name).get().addOnSuccessListener { result ->
                if (result.exists()) {
                    if (delete == 3) {
                        val doc = db.collection("Attractions").document(name)
                        doc.get().addOnSuccessListener { results ->
                            if (results.exists())
                                doc.delete()
                        }
                    }
                    else{
                        val v = db.collection("Attractions").document(name)
                        db.runTransaction { transaction ->
                            val doc = transaction.get(v)
                            if (doc.exists()) {
                                val deleteapproved = (doc.getLong("DeleteApproved") ?: 0) + 1
                                transaction.update(v, "DeleteApproved", deleteapproved)
                                null
                            } else
                                throw FirebaseFirestoreException(
                                    "Doesn't exist",
                                    FirebaseFirestoreException.Code.UNAVAILABLE
                                )
                        }.addOnCompleteListener{result ->
                            onResult(result.exception)
                        }

                    }
                }
            }

        }


        //Reviews
        fun addReviews(title: String, desc: String, image: String, attraction: String, rating: Number, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore

            val review = hashMapOf(
                "Title" to title,
                "Description" to desc,
                "Image" to image,
                "Attraction" to db.document("Attractions/$attraction"),
                "User" to db.document("Users/${AuthUtil.currentUser!!.uid}"),
                "Rating" to rating
            )

            val doc = db.collection("Reviews").document()

            doc.set(review)
                .addOnSuccessListener {
                    onResult(null)
                }
                .addOnFailureListener { e ->
                    onResult(e)
                }
        }



        fun getAttractionDetails(userGeo: GeoPoint, name: String, onResult: (Attraction?) -> Unit) {

            val db = Firebase.firestore

            // Attempt to fetch the single location document based on country and region
            val doc = db.collection("Attractions").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {


                    val description = document["Description"] as? String ?: "No description available"
                    val geoPoint = document["Coordinates"] as? GeoPoint ?: GeoPoint(0.0, 0.0)
                    val numApproved = document["Approved"] as? Number ?: 0
                    val category = document["Category"] as? String ?: "Unknown"
                    val location = document["Location"] as? String ?: "Unknown"
                    val numDeleteApproved = document["DeleteApproved"] as? Number ?: 0
                    val imageUrls = document["Images"] as? List<String> ?: emptyList()
                    val distanceInKmFromCurrent = calculateDistance(userGeo, geoPoint)

                    val userRef = document["User"] as? DocumentReference

                    userRef?.get()?.addOnSuccessListener { userDocument ->
                        val userName = userDocument.getString("Name") ?: "Unknown"

                    getReviewsByAttraction(name) { linkedReviews ->
                        val attractionDetails = Attraction(
                            reviews = linkedReviews,
                            name = name,
                            numApproved = numApproved.toInt(),
                            distanceInKmFromCurrent = distanceInKmFromCurrent,
                            description = description,
                            coordinates = geoPoint,
                            imageUrls = imageUrls,
                            userRef = userName,
                            numDeleteApproved = numDeleteApproved.toInt(),
                            category = category,
                            location = location,
                            averageRating = String.format("%.1f", linkedReviews.map { it.rating.toInt() }.average()).replace(",",".").toFloat(),
                            numReviews = linkedReviews.size,
                        )

                        onResult(attractionDetails)
                    }
                    }?.addOnFailureListener { e ->
                        onResult(null)
                    }

                } else {
                    onResult(null)
                }
            }.addOnFailureListener {
                onResult(null)
            }
        }

        fun getReviewsByAttraction(attraction: String, onResult: (List<Review>) -> Unit) {
            val db = Firebase.firestore
            db.collection("Reviews")
                .whereEqualTo("Attraction", db.document("Attractions/$attraction"))
                .get()
                .addOnSuccessListener { result ->
                    val reviews = mutableListOf<Review>()

                    for (document in result) {
                        val title = document.getString("Title") ?: ""
                        val description = document.getString("Description") ?: ""
                        val image = document.getString("Image") ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"
                        val rating = document.getLong("Rating") ?: 0.0
                        val attraction = document.getDocumentReference("Attraction")?.id ?: ""
                        val user = document.getDocumentReference("User")?.id ?: ""



                        val review = Review(title, description, image, rating, attraction, user)
                        reviews.add(review)
                    }

                    onResult(reviews)
                }
                .addOnFailureListener { exception ->
                    onResult(emptyList())
                }
        }

        fun getAllDetailsFromReviewsByUser(onResult: (List<ReviewsDetails>, Throwable?) -> Unit) {
            val db = Firebase.firestore
            val userUid = AuthUtil.currentUser?.uid

            if (userUid == null) {
                // Usuário não autenticado, retornar erro
                onResult(emptyList(), Throwable("User not authenticated"))
                return
            }

            db.collection("Reviews")
                .whereEqualTo("User", db.document("Users/$userUid"))
                .get()
                .addOnSuccessListener { result ->
                    val reviewDetailsList = mutableListOf<ReviewsDetails>()

                    for (document in result) {
                        val reviewId = document.id
                        val com = document.getString("Description") ?: ""
                        val title = document.getString("Title") ?: ""
                        val rating = document.getLong("Rating") ?: 0
                        val attractionId = document.getDocumentReference("Attraction")?.id ?: ""

                        val reviewDetails = ReviewsDetails(reviewId, attractionId, title, com, rating.toInt())
                        reviewDetailsList.add(reviewDetails)

                        if (reviewDetailsList.size == result.size()) {
                            onResult(reviewDetailsList, null)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    onResult(emptyList(), exception)
                }
        }

        fun deleteReview(reviewId: String, onResult: (Throwable?) -> Unit) {
            val db = Firebase.firestore
            val doc = db.collection("Reviews").document(reviewId)

            doc.delete()
                .addOnSuccessListener {
                    onResult(null)
                }
                .addOnFailureListener { e ->
                    onResult(e)
                }
        }

        fun getReviewDetails(name: String, onResult: (Review?) -> Unit) {

            val db = Firebase.firestore

            val doc = db.collection("Review").document(name)
                .get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val title = document["Title"] as? String ?: "Unknown"
                    val description =
                        document["Description"] as? String ?: "No description available"
                    val imageUrl = document["Image"] as? String
                        ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"
                    val userRef = document["User"] as? String ?: "Unknown"
                    val linkedAttractionRef = document["Attraction"] as? String ?: "Unknown"
                    val rating = document["Rating"] as? Number ?: 0

                    val review = Review(
                        title = title,
                        description = description,
                        imageUrl = imageUrl,
                        userRef = userRef,
                        rating = rating.toInt(),
                        linkedAttractionRef = linkedAttractionRef,
                    )

                    onResult(review)
                } else {
                    onResult(null)
                }
            }.addOnFailureListener {
                onResult(null)
            }
        }

        fun calculateDistance(startGeo: GeoPoint, endGeo: GeoPoint): Float {
            val earthRadius = 6371.0 // Radius of the earth in km
            val latDistance = Math.toRadians(endGeo.latitude - startGeo.latitude)
            val lonDistance = Math.toRadians(endGeo.longitude - startGeo.longitude)
            val a = sin(latDistance / 2).pow(2) +
                    (cos(Math.toRadians(startGeo.latitude)) * cos(Math.toRadians(endGeo.latitude)) *
                            sin(lonDistance / 2).pow(2))
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            val distance = earthRadius * c // calculate the distance


            // Round to one decimal place and convert to Float
            return String.format("%.1f", distance).replace(',', '.').toFloat()
        }

        fun getAttractionsByLocation(userGeo: GeoPoint, location: String, onResult: (List<Attraction>) -> Unit) {
                val db = Firebase.firestore
                db.collection("Attractions")
                .whereEqualTo("Location", location)
                .get()
                .addOnSuccessListener { result ->
                    val attractions = mutableListOf<Attraction>()
                    val countDownLatch = CountDownLatch(result.size())
                    result.forEach { document ->
                        getAttractionDetails(userGeo=userGeo, name = document.id) { attraction ->
                            if (attraction != null) {
                                attractions.add(attraction)
                            }
                            countDownLatch.countDown()
                        }
                    }
                    countDownLatch.await()
                    onResult(attractions)
                }
                .addOnFailureListener {
                    onResult(emptyList())
                }
        }

        fun getAttractionsByCategory(userGeo: GeoPoint, category: String, onResult: (List<Attraction>) -> Unit) {
            val db = Firebase.firestore
            db.collection("Attractions")
                .whereEqualTo("Category", category)
                .get()
                .addOnSuccessListener { result ->
                    val attractions = mutableListOf<Attraction>()
                    val countDownLatch = CountDownLatch(result.size())
                    result.forEach { document ->
                        getAttractionDetails(userGeo=userGeo, name = document.id) { attraction ->
                            if (attraction != null) {
                                attractions.add(attraction)
                            }
                            countDownLatch.countDown()
                        }
                    }
                    countDownLatch.await()
                    onResult(attractions)
                }
                .addOnFailureListener {
                    onResult(emptyList())
                }
        }

        fun addApprovedAttractions(AttractionName: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val currentUser = AuthUtil.currentUser
            if (currentUser != null) {
                val userDocRef = db.collection("VoteOnAttraction").document(currentUser.uid)

                userDocRef.get().addOnSuccessListener { result ->
                    if (result.exists()) {

                        val aux = result.get("AttractionsApproved") as? List<String>

                        if (aux != null) {
                            for (i in aux) {
                                if (i == AttractionName) {
                                    val error = Throwable("Attraction already approved.")
                                    onResult(error)
                                    return@addOnSuccessListener
                                }
                            }
                        }

                        userDocRef.update(
                            "AttractionsApproved",
                            FieldValue.arrayUnion(AttractionName)
                        ).addOnSuccessListener {
                            val sucess = Throwable("Voted Sucessfully")
                                onResult(sucess)
                            }
                            .addOnFailureListener {
                                val fail = Throwable("Fail to Vote")
                                onResult(fail)
                            }
                    } else {
                        userDocRef.set(hashMapOf("AttractionsApproved" to listOf(AttractionName)))
                            .addOnSuccessListener {
                                val sucess = Throwable("Voted Sucessfully")
                                onResult(sucess)
                            }
                            .addOnFailureListener {
                                val fail = Throwable("Fail to Vote")
                                onResult(fail)
                            }
                    }
                    updateApprovedAttraction(AttractionName) {}
                }
            }else {
                val error = Throwable("User not authenticated.")
                onResult(error)
            }
        }
        fun addApprovedToDeleteAttractions(AttractionName: String, onResult: (Throwable?) -> Unit){

            val db = Firebase.firestore
            val currentUser = AuthUtil.currentUser
            if (currentUser != null) {
                val userDocRef = db.collection("VoteOnAttraction").document(currentUser.uid)

                userDocRef.get().addOnSuccessListener { result ->
                    if (result.exists()) {

                        val aux = result.get("AttractionsApprovedToDelete") as? List<String>

                        if (aux == null){
                            userDocRef.set(hashMapOf("AttractionsApprovedToDelete" to listOf(AttractionName)))
                                .addOnSuccessListener {
                                    val sucess = Throwable("Voted Sucessfully")
                                    onResult(sucess)
                                }
                                .addOnFailureListener {
                                    val fail = Throwable("Fail to Vote")
                                    onResult(fail)
                                }
                        }else {
                            for (i in aux) {
                                if (i == AttractionName) {
                                    val error = Throwable("Attraction already approved.")
                                    onResult(error)
                                    return@addOnSuccessListener
                                }
                            }

                            userDocRef.update(
                                "AttractionsApprovedToDelete",
                                FieldValue.arrayUnion(AttractionName)
                            ).addOnSuccessListener {
                                val sucess = Throwable("Voted Sucessfully")
                                onResult(sucess)
                            }
                                .addOnFailureListener {
                                    val fail = Throwable("Fail to Vote")
                                    onResult(fail)
                                }
                        }
                    }
                    updateDeleteApprovedAttraction(AttractionName) {}
                }
            }else {
                val error = Throwable("User not authenticated.")
                onResult(error)
            }
        }

        fun getToDeleteBoolean(name: String, onResult : (Boolean) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Attractions").document(name)

            db.runTransaction { transaction ->
                val doc = transaction.get(v)
                if (doc.exists()) {
                    val todelete = doc.getBoolean("ToDelete") ?: false

                    onResult(todelete)
                    null
                } else
                    throw FirebaseFirestoreException(
                        "Doesn't exist",
                        FirebaseFirestoreException.Code.UNAVAILABLE
                    )
            }
        }

        fun addApprovedLocations(LocationName: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val currentUser = AuthUtil.currentUser
            if (currentUser != null) {
                val userDocRef = db.collection("VoteOnLocation").document(currentUser.uid)

                userDocRef.get().addOnSuccessListener { result ->
                    if (result.exists()) {

                        val aux = result.get("LocationsApproved") as? List<String>

                        for (i in aux!!) {
                            if (i == LocationName) {
                                val error = Throwable("Location already approved.")
                                onResult(error)
                                return@addOnSuccessListener
                            }
                        }

                        userDocRef.update(
                            "LocationsApproved",
                            FieldValue.arrayUnion(LocationName)
                        ).addOnSuccessListener {
                            val sucess = Throwable("Voted Sucessfully")
                            onResult(sucess)
                        }
                            .addOnFailureListener {
                                val fail = Throwable("Fail to Vote")
                                onResult(fail)
                            }
                    } else {
                        userDocRef.set(hashMapOf("LocationsApproved" to listOf(LocationName)))
                            .addOnSuccessListener {
                                val sucess = Throwable("Voted Sucessfully")
                                onResult(sucess)
                            }
                            .addOnFailureListener {
                                val fail = Throwable("Fail to Vote")
                                onResult(fail)
                            }
                    }
                    updateApprovedLocation(LocationName) {}
                }
            }else {
                val error = Throwable("User not authenticated.")
                onResult(error)
            }
        }
        fun addApprovedCategories(CategoriesName: String, onResult: (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val currentUser = AuthUtil.currentUser
            if (currentUser != null) {
                val userDocRef = db.collection("VoteOnCategories").document(currentUser.uid)

                userDocRef.get().addOnSuccessListener { result ->
                    if (result.exists()) {

                        val aux = result.get("CategoriesApproved") as? List<String>

                        for (i in aux!!) {
                            if (i == CategoriesName) {
                                val error = Throwable("Categories already approved.")
                                onResult(error)
                                return@addOnSuccessListener
                            }
                        }

                        userDocRef.update(
                            "CategoriesApproved",
                            FieldValue.arrayUnion(CategoriesName)
                        ).addOnSuccessListener {
                            val sucess = Throwable("Voted Sucessfully")
                            onResult(sucess)
                        }
                            .addOnFailureListener {
                                val fail = Throwable("Fail to Vote")
                                onResult(fail)
                            }
                    } else {
                        userDocRef.set(hashMapOf("CategoriesApproved" to listOf(CategoriesName)))
                            .addOnSuccessListener {
                                val sucess = Throwable("Voted Sucessfully")
                                onResult(sucess)
                            }
                            .addOnFailureListener {
                                val fail = Throwable("Fail to Vote")
                                onResult(fail)
                            }
                    }
                    updateApprovedCategory(CategoriesName) {}
                }
            }else {
                val error = Throwable("User not authenticated.")
                onResult(error)
            }
        }

    }

}