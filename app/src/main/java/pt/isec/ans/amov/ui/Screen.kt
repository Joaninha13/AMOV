package pt.isec.ans.amov.ui

import pt.isec.ans.amov.dataStructures.Location

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddAttractions : Screen("addAttraction")
    object AddLocations : Screen("addLocation")
    object AddCategories : Screen("addCategory")
    object EditAttraction : Screen("editAttraction")
    object EditLocation : Screen("editLocation")
    object EditCategory : Screen("editCategory")
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

    object ViewAttraction : Screen("viewAttraction")
    object ViewLocation : Screen("viewLocation")
    object ViewCategory : Screen("viewCategory")
    object ViewAccount : Screen("viewAccount")

    object CreditsScreen : Screen("creditsScreen")

}