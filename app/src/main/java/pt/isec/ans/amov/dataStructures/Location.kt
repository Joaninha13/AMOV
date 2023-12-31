package pt.isec.ans.amov.dataStructures

import com.google.firebase.firestore.GeoPoint

data class Location(
    val country: String = "",
    val region: String = "",
    val numAttractions: Int = 0,
    val numApproved: Int = 0,
    val linkedAttractions: List<Attraction> = emptyList(),
    val distanceInKmFromCurrent: Float = 0f,
    val description: String = "",
    val coordinates: GeoPoint = GeoPoint(0.0, 0.0),
    val userRef: String = "",
    val imageUrl: String = ""
)
