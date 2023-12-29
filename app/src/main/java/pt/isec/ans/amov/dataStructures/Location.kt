package pt.isec.ans.amov.dataStructures

import com.google.firebase.firestore.GeoPoint

data class Location(
    val country: String,
    val region: String,
    val numAttractions: Int,
    val distanceInKmFromCurrent: Float,
    val description: String,
    val coordinates: GeoPoint,
    val imageUrl: String
)
