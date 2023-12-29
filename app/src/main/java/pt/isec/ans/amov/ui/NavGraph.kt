package pt.isec.ans.amov.ui

import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
    NavHost(navController = navController, startDestination = Screen.Home.route)
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
            EditAttraction(navController = navController)
        }
        composable(Screen.EditLocation.route) {
            EditLocation(navController = navController)
        }
        composable(Screen.EditCategory.route) {
            EditCategory(navController = navController)
        }
        composable(Screen.EditPersonalData.route) {
            EditPersonalData(navController = navController)
        }
        composable(Screen.InfoAttraction.route) {
            InfoAttraction(navController = navController)
        }
        composable(Screen.InfoLocation.route) {
            InfoLocation(navController = navController)
        }
        composable(Screen.InfoCategory.route) {
            InfoCategory(navController = navController)
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