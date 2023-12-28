package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.runtime.Composable

data class FilterFields(
    var category: String = "",
    var location: String = "",
    var country: String = "",
    var approved: Boolean = false,
    var minDistance: Int = 0,
    var maxDistance: Int = 100
) {
    override fun toString(): String {
        return "FilterFields(category='$category', location='$location', country='$country', approved=$approved)"
    }


}
