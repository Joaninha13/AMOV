package pt.isec.ans.amov.dataStructures

data class Review(
    val title: String,
    val description: String,
    val imageUrl: String,
    val rating: Number,
    val linkedAttractionRef: String,
    val userRef: String,
)
