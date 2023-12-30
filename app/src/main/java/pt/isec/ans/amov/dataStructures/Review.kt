package pt.isec.ans.amov.dataStructures

data class Review(
    val name: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val rating: Int,
    val linkedAttractionRef: String,
    val userRef: String,
)
