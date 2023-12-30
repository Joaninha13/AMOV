package pt.isec.ans.amov.Utils.FireBase

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import pt.isec.ans.amov.dataStructures.Category
import pt.isec.ans.amov.dataStructures.Location
import java.util.UUID
import kotlin.math.*

class StorageUtil {

    //forma de criar os users!
    /*if (FireAuthUtil.currentUser != null) {
        db.collection("Users").document(FireAuthUtil.currentUser!!.uid).set(User)
            .addOnCompleteListener { result ->
                onResult(result.exception)
            }
    }*/

    //forma de ir a conteudo de referencias
    /*val db = Firebase.firestore
    db.collection("Scores")
    .get()
    .addOnSuccessListener { result ->
        for (document in result) {
            val userReference = document.getDocumentReference("First")
            if (userReference != null) {
                userReference.get().addOnSuccessListener { userDocument ->
                    val name = userDocument.getString("firstfirstone")
                    Log.i(TAG, "$name")
                }

            }
        }
    }.addOnFailureListener { e ->
        // Tratando falhas na leitura das categorias
    }*/


    //saber se uma tabela existe ja ou nao com dados. se nao existir cria e mete os dados
    /*val doc = db.collection("Scores").document("Level1")

    doc.get().addOnSuccessListener { result ->
        if (result.exists()) {
            Log.i(ContentValues.TAG, "DocumentSnapshot data: ${result.data}")
        } else {
            Log.i(ContentValues.TAG, "No such document")
            doc.set(scores)
        }
    }.addOnFailureListener { exception ->
        Log.i(ContentValues.TAG, "get failed with ", exception)
    }*/

    companion object {

        //ver depois de testar o createUserWithEmail
        fun addUser(name: String, image: String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val User = hashMapOf(
                "Name" to name,
                "Photo" to image,
            )

            if (AuthUtil.currentUser != null) {
                db.collection("Users").document(name).set(User)
                    .addOnCompleteListener { result ->
                        onResult(result.exception)
                    }
            }
        }

        fun uploadImageToFirebaseStorage(imageUri: Uri, onSuccess: (String) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference
            val imagesRef: StorageReference = storageRef.child("images/${UUID.randomUUID()}")

            imagesRef.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Imagem enviada com sucesso, obter a URL da imagem
                    imagesRef.downloadUrl.addOnSuccessListener { uri ->
                        onSuccess(uri.toString())
                    }
                }
        }

        fun uploadImagesToFirebaseStorage(imageUris: List<Uri>, onSuccess: (List<String>) -> Unit) {
            val storage = FirebaseStorage.getInstance()
            val storageRef: StorageReference = storage.reference

            val imagesList : MutableList<String> = mutableListOf()

            var completedCount = 0

            for (imageUri in imageUris) {
                val imagesRef: StorageReference = storageRef.child("images/${UUID.randomUUID()}")
                imagesRef.putFile(imageUri)
                    .addOnSuccessListener { taskSnapshot ->
                        // Imagem enviada com sucesso, obter a URL da imagem
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
        fun addCategory(name: String, desc : String, logo: String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore

            val category = hashMapOf(
                "Name" to name,
                "Description" to desc,
                "Approved" to 0,
                "Logo" to logo,// mudar isto para guardar o path da imagem guardada no storage
                "User" to db.document("Users/${AuthUtil.currentUser!!.uid}")
            )

            val doc = db.collection("Category").document(name)
            doc.get().addOnSuccessListener { result ->
                if (!result.exists())
                    doc.set(category)
            }
        }
        fun getAllCategorysDocumentsNames(onResult : (List<String>) -> Unit) {

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
        fun getAllFromOneCategory(name: String, onResult : (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Category").document(name)

            doc.get().addOnSuccessListener { result ->
                if (result.exists())
                    onResult(result.data!!.entries?.sortedBy { it.key }?.map { it.value?.toString() ?: "" } ?: emptyList())
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun updateCategory(categoryName: String, name: String, desc : String, logo: String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Category").document(categoryName)


            if (categoryName != name) {
                addCategory(name, desc, logo) {}
                deleteCategory(categoryName) {}
            }
            else {
                db.runTransaction { transaction ->
                    val doc = transaction.get(v)
                    if (doc.exists()) {
                        transaction.update(v, "Description", desc)
                        transaction.update(v, "Logo", logo)
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

        fun updateApprovedCategory(onResult : (Throwable?) -> Unit, name: String) {

            val db = Firebase.firestore
            val v = db.collection("Categories").document(name)

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
        fun deleteCategory(name: String, onResult : (Throwable?) -> Unit) {

            //As categorias e as localizações podem ser eliminadas pelos seus autores, desde que não possuam qualquer local de interesse.

            val db = Firebase.firestore

            val docAtt = db.collection("Attractions").whereEqualTo("Category", db.document("Category/$name"))

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
        fun addLocation(country: String, region : String, desc: String, coordinates: GeoPoint, image : String, onResult : (Throwable?) -> Unit) {

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
        fun getAllLocationsDocumentsNames(onResult : (List<String>) -> Unit) {

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
        fun getAllLocationsDocumentsCoordinates(onResult : (List<GeoPoint>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Location")

            doc.get().addOnSuccessListener { result ->
                val coordinates = ArrayList<GeoPoint>()
                for (document in result) {
                    val geoPoint = document["Coordinates"] as? GeoPoint

                    if(geoPoint != null){
                        coordinates.add(GeoPoint(geoPoint.latitude, geoPoint.longitude))
                    }
                }
                onResult(coordinates)
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }
        fun updateApprovedLocation(onResult : (Throwable?) -> Unit, country: String, region : String) {

            val db = Firebase.firestore
            val v = db.collection("Location").document("${country}_${region}")

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

            //As categorias e as localizações podem ser eliminadas pelos seus autores, desde que não possuam qualquer local de interesse.

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
                "Descripiton" to desc,
                "Coordinates" to coordinates,
                "Approved" to 0,
                "DelteApproved" to 0,
                "Category" to db.document("Categories/$category"),
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

        fun getAllFromOneLocation(name: String, onResult : (List<String>) -> Unit) {

            val db = Firebase.firestore
            val doc = db.collection("Location").document(name)

            doc.get().addOnSuccessListener { result ->
                if (result.exists())
                    onResult(result.data!!.entries?.sortedBy { it.key }?.map { it.value?.toString() ?: "" } ?: emptyList())
            }.addOnFailureListener { exception ->
                onResult(arrayListOf())
            }
        }

        fun updateLocation(locationName: String,country: String, region : String, desc: String, coordinates: GeoPoint, image : String, onResult : (Throwable?) -> Unit) {

            val db = Firebase.firestore
            val v = db.collection("Location").document("${country}_${region}")

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
        @SuppressLint("SuspiciousIndentation")
        fun deleteAttraction(onResult : (Throwable?) -> Unit, name: String) {

            //As Attractions podem ser eliminados pelos seus autores, mas depois de possuírem votações
            //só o poderão ser com a aprovação de remoção por 3 utilizadores

            val db = Firebase.firestore

            db.collection("Attractions").document(name).get().addOnSuccessListener { result ->
                    if (result.exists()) {
                        val approved = result.getLong("Approved")
                        val delete = result.getLong("DeleteApproved")
                            if (approved!! >= 2 || delete!! >= 3) {
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






        fun getLocationDetails(userGeo: GeoPoint, name: String, onResult: (Location?) -> Unit) {
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

            // Attempt to fetch the single location document based on country and region
            val doc = db.collection("Location").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val fetchedCountry = document["Country"] as? String ?: "Unknown"
                    val fetchedRegion = document["Region"] as? String ?: "Unknown"

                    //Change later TODO
                    val numAttractions = document["NumAttractions"] as? Int ?: 0

                    val description = document["Description"] as? String ?: "No description available"
                    val geoPoint = document["Coordinates"] as? GeoPoint ?: GeoPoint(0.0, 0.0)
                    val imageUrl = document["Image"] as? String ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"


                    val distanceInKmFromCurrent = calculateDistance(userGeo, geoPoint)

                    val location = Location(
                        country = fetchedCountry,
                        region = fetchedRegion,
                        numAttractions = numAttractions,
                        distanceInKmFromCurrent = distanceInKmFromCurrent,
                        description = description,
                        coordinates = geoPoint,
                        imageUrl = imageUrl
                    )

                    onResult(location)
                } else {
                    onResult(null)  // Handle case where the document does not exist
                }
            }.addOnFailureListener {
                onResult(null)  // Handle failure case
            }
        }

        fun getCategoryDetails(name: String, onResult: (Category?) -> Unit) {

            val db = Firebase.firestore

            // Attempt to fetch the single location document based on country and region
            val doc = db.collection("Category").document(name)
            doc.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    val fetchedName = document["Name"] as? String ?: "Unknown"
                    val description = document["Description"] as? String ?: "No description available"
                    val logoUrl = document["Logo"] as? String ?: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/800px-Image_not_available.png?20210219185637"


                    //Change later TODO
                    val numAttractions = document["NumAttractions"] as? Int ?: 0


                    val category = Category(
                        name = fetchedName,
                        description = description,
                        logoUrl = logoUrl,
                        numAttractions = numAttractions
                    )

                    onResult(category)
                } else {
                    onResult(null)  // Handle case where the document does not exist
                }
            }.addOnFailureListener {
                onResult(null)  // Handle failure case
            }
        }














    }

}