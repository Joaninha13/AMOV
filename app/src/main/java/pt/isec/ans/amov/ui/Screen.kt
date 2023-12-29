package pt.isec.ans.amov.ui

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddAttractions : Screen("addAttraction")
    object AddLocations : Screen("addLocation")
    object AddCategories : Screen("addCategory")
    object EditAttraction : Screen("editAttraction")
    object EditLocation : Screen("editLocation")
    object EditCategory : Screen("editCategory")
    object EditPersonalData : Screen("editPersonalData")
    object InfoAttraction : Screen("infoAttraction")
    object InfoLocation : Screen("infoLocation")
    object InfoCategory : Screen("infoCategory")
    object ViewAttraction : Screen("viewAttraction")
    object ViewLocation : Screen("viewLocation")
    object ViewCategory : Screen("viewCategory")
    object ViewAccount : Screen("viewAccount")

}