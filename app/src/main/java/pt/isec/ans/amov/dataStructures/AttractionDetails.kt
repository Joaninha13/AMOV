package pt.isec.ans.amov.dataStructures

data class AttractionDetails(
    val name : String,
    val Rating : String,
    val numReviews : Int,
    val ApprovedsDelete : Int,
    val toDelete: Boolean,
    val image: List<String>,
)