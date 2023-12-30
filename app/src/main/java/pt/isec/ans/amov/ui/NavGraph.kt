package pt.isec.ans.amov.ui

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pt.isec.ans.amov.ui.Screens.AddAttraction
import pt.isec.ans.amov.ui.Screens.AddCategory
import pt.isec.ans.amov.ui.Screens.AddLocation
import pt.isec.ans.amov.ui.Screens.EditAttraction
import pt.isec.ans.amov.ui.Screens.EditCategory
import pt.isec.ans.amov.ui.Screens.EditLocation
import pt.isec.ans.amov.ui.Screens.EditPersonalData
import pt.isec.ans.amov.ui.Screens.Home
import pt.isec.ans.amov.ui.Screens.InfoAttraction
import pt.isec.ans.amov.ui.Screens.InfoCategory
import pt.isec.ans.amov.ui.Screens.InfoLocation
import pt.isec.ans.amov.ui.Screens.ViewAccount
import pt.isec.ans.amov.ui.Screens.ViewAttractions
import pt.isec.ans.amov.ui.Screens.ViewCategories
import pt.isec.ans.amov.ui.Screens.ViewLocations
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel
) {
    NavHost(navController = navController, startDestination = Screen.EditLocation.route)
    {
        composable(Screen.Home.route) {
            Home(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB)
        }
        composable(Screen.AddAttractions.route) {
            AddAttraction(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB)
        }
        composable(Screen.AddLocations.route) {
            AddLocation(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB)
        }
        composable(Screen.AddCategories.route) {
            AddCategory(navController = navController,viewModelFB = viewModelFB)
        }
        composable(Screen.EditAttraction.route) {
            EditAttraction(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB)
        }
        composable(Screen.EditLocation.route) {
            EditLocation(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, nameToEdit = "teste")
        }
        composable(Screen.EditCategory.route) {
            EditCategory(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, nameToEdit = "teste")
        }
        composable(Screen.EditPersonalData.route) {
            EditPersonalData(navController = navController)
        }
        composable(
            route = Screen.InfoAttraction.route,
            arguments = listOf(navArgument("attractionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val attractionId = backStackEntry.arguments?.getString("attractionId")
            InfoAttraction(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, attractionId = attractionId ?: "")
        }
        composable(
            route = Screen.InfoLocation.route,
            arguments = listOf(navArgument("locationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId")
            InfoLocation(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, locationId = locationId ?: "")
        }
        composable(
            route = Screen.InfoCategory.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            InfoCategory(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, categoryId = categoryId ?: "")
        }
        composable(Screen.ViewAttraction.route) {
            ViewAttractions(navController = navController)
        }
        composable(Screen.ViewLocation.route) {
            ViewLocations(navController = navController)
        }
        composable(Screen.ViewCategory.route) {
            ViewCategories(navController = navController)
        }
        composable(Screen.ViewAccount.route) {
            ViewAccount(navController = navController)
        }

    }
}