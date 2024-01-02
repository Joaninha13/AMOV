package pt.isec.ans.amov.dataStructures

import android.icu.text.CaseMap.Title
import org.w3c.dom.Comment

data class ReviewsDetails(
    val reviewId: String,
    val attractionId: String,
    val title: String,
    val comment: String,
    val rating: Int,
)
