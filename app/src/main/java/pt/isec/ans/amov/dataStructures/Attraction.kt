package pt.isec.ans.amov.dataStructures

import com.google.firebase.firestore.GeoPoint

data class Attraction (
    val name: String,
    val coordinates: GeoPoint,
    val description: String,
    val category: String,
    val imageUrlList: List<String>
)
