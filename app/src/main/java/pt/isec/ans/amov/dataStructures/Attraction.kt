package pt.isec.ans.amov.dataStructures

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint

data class Attraction(
    val name: String = "",
    val numApproved: Int = 0,
    val numDeleteApproved: Int = 0,
    val distanceInKmFromCurrent: Float = 0f,
    val category: String = "",
    val location: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val description: String = "",
    val userRef: String = "",
    val imageUrls: List<String> = emptyList(),
    val numReviews: Int = 0,
    val averageRating: Float = 0f,
    val reviews: List<Review> = emptyList(),
    val imageUrlList: List<String> = emptyList()
)
