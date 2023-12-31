package pt.isec.ans.amov.ui.Components.Nav

import pt.isec.ans.amov.dataStructures.Attraction
import pt.isec.ans.amov.dataStructures.Category
import pt.isec.ans.amov.dataStructures.Location

sealed class SearchResultItem {
    data class AttractionItem(val attraction: Attraction) : SearchResultItem()
    data class LocationItem(val location: Location) : SearchResultItem()
    data class CategoryItem(val category: Category) : SearchResultItem()
}
