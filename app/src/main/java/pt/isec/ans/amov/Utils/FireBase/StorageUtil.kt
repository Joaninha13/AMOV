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
import java.util.UUID

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
                "Descripiton" to desc,
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
        fun deleteCategory(onResult : (Throwable?) -> Unit, name: String) {

            //As categorias e as localizações podem ser eliminadas pelos seus autores, desde que não possuam qualquer local de interesse.

            val db = Firebase.firestore

            val docAtt = db.collection("Attractions").whereEqualTo("Category", db.document("Categories/$name"))

            docAtt.get().addOnSuccessListener { result ->
                if (result.isEmpty) {
                    val doc = db.collection("Categories").document(name)
                    doc.get().addOnSuccessListener { results ->
                        if (results.exists())
                            doc.delete()
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
        fun deleteLocation(onResult : (Throwable?) -> Unit, country: String, region : String) {

            //As categorias e as localizações podem ser eliminadas pelos seus autores, desde que não possuam qualquer local de interesse.

            val db = Firebase.firestore

            val docAtt = db.collection("Attractions").whereEqualTo("Location", db.document("Location/${country}_${region}"))

            docAtt.get().addOnSuccessListener { result ->
                if (result.isEmpty) {
                    val doc = db.collection("Location").document("${country}_${region}")
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
        fun updateApprovedAttraction(onResult : (Throwable?) -> Unit, name: String) {

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

    }

}