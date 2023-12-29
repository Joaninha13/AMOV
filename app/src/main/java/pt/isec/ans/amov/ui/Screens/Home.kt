package pt.isec.ans.amov.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.LocationMapsTheme

@Composable
fun Home(
    navController: NavHostController,
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel
) {
    LocationMapsTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MainMapScreen(viewModelL = viewModelL, navController = navController, viewModelFB = viewModelFB)
        }
    }
}