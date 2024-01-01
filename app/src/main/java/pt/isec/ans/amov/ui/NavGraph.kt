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
import pt.isec.ans.amov.ui.Screens.LoginScreen
import pt.isec.ans.amov.ui.Screens.RegisterAcc
import pt.isec.ans.amov.ui.Screens.Review
import pt.isec.ans.amov.ui.Screens.ViewAccount
import pt.isec.ans.amov.ui.Screens.ViewAttractions
import pt.isec.ans.amov.ui.Screens.ViewCategories
import pt.isec.ans.amov.ui.Screens.ViewLocations
import pt.isec.ans.amov.ui.Screens.CreditsScreen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel
) {
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route)
    {

        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController, viewModel = viewModelFB){ navController.navigate(Screen.Home.route)}
        }
        composable(Screen.RegisterAcc.route) {
            RegisterAcc(navController = navController, viewModel = viewModelFB)
        }
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
        composable(
            route = Screen.EditAttraction.route,
            arguments = listOf(navArgument("attractionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val attractionId = backStackEntry.arguments?.getString("attractionId")
            EditAttraction(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, nameToEdit = attractionId ?: "")
        }
        composable(
            route = Screen.EditLocation.route,
            arguments = listOf(navArgument("locationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("locationId")
            EditLocation(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, nameToEdit = locationId ?: "")
        }
        composable(
            route = Screen.EditCategory.route,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            EditCategory(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, nameToEdit = categoryId ?: "")
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

        composable(
            route = Screen.Review.route,
            arguments = listOf(navArgument("attractionNames") { type = NavType.StringType })
        ) { backStackEntry ->
            val attractionNames = backStackEntry.arguments?.getString("attractionNames")
            Review(navController = navController, viewModelL = viewModelL, viewModelFB = viewModelFB, attractionNames = attractionNames ?: "")
        }

        composable(Screen.ViewAttraction.route) {
            ViewAttractions(navController = navController, viewModel = viewModelFB)
        }
        composable(Screen.ViewLocation.route) {
            ViewLocations(navController = navController, viewModel = viewModelFB)
        }
        composable(Screen.ViewCategory.route) {
            ViewCategories(navController = navController, viewModel = viewModelFB)
        }
        composable(Screen.ViewAccount.route) {
            ViewAccount(navController = navController)
        }
        composable(Screen.CreditsScreen.route) {
            CreditsScreen(navController = navController)
        }
    }
}