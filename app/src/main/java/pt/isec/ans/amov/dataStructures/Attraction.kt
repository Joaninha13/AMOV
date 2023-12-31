package pt.isec.ans.amov.dataStructures

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Attraction(
    val name: String,
    val numApproved: Int,
    val numDeleteApproved: Int,
    val distanceInKmFromCurrent: Float,
    val category: String,
    val location: String,
    val coordinates: GeoPoint,
    val description: String,
    val userRef: DocumentReference?,
    val imageUrls: List<String>,
    val numReviews: Int,
    val averageRating: Float,
    val reviews: List<Review>,
    val imageUrlList: List<String>
)
