package pt.isec.ans.amov.dataStructures

import com.google.firebase.firestore.GeoPoint

data class Category(
    val name: String,
    val description: String,
    val logoUrl: String,
    val numAttractions: Int,
)
