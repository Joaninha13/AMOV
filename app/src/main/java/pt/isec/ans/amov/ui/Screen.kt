package pt.isec.ans.amov.ui

import pt.isec.ans.amov.dataStructures.Location

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddAttractions : Screen("addAttraction")
    object AddLocations : Screen("addLocation")
    object AddCategories : Screen("addCategory")
    object EditAttraction : Screen("editAttraction/{attractionId}") {
        fun createRoute(attractionId: String) = "editAttraction/$attractionId"
    }
    object EditLocation : Screen("editLocation/{locationId}") {
        fun createRoute(locationId: String) = "editLocation/$locationId"
    }
    object EditCategory : Screen("editCategory/{categoryId}") {
        fun createRoute(categoryId: String) = "editCategory/$categoryId"
    }
    object EditPersonalData : Screen("editPersonalData")
    object InfoAttraction : Screen("infoAttraction/{attractionId}") {
        fun createRoute(attractionId: String) = "infoAttraction/$attractionId"
    }
    object InfoLocation : Screen("infoLocation/{locationId}") {
        fun createRoute(locationId: String) = "infoLocation/$locationId"
    }
    object InfoCategory : Screen("infoCategory/{categoryId}") {
        fun createRoute(categoryId: String) = "infoCategory/$categoryId"
    }

    object Review : Screen("Review")

    object ViewAttraction : Screen("viewAttraction")
    object ViewLocation : Screen("viewLocation")
    object ViewCategory : Screen("viewCategory")
    object ViewAccount : Screen("viewAccount")

}