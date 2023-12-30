package pt.isec.ans.amov.dataStructures

data class Category(
    val name: String,
    val description: String,
    val logoUrl: String,
    val linkedAttractions: List<Attraction>,
    val numApproved: Int,
    val numAttractions: Int,
    val userRef: String,
)
