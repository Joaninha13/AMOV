package pt.isec.ans.amov.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pt.isec.ans.amov.ui.Components.MapScreen
import pt.isec.ans.amov.ui.Components.Nav.NavBar
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel


@Composable
fun MainMapScreen(viewModel: LocationViewModel){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Page Column
        Column(
            modifier = Modifier
                .width(360.dp)
                .height(804.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // MapScreen
                MapScreen(viewModel = viewModel)

                // NavBar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, top = 35.dp, end = 10.dp, bottom = 10.dp)
                        .zIndex(2f) // Garante que a NavBar fique acima do MapScreen
                        .background(Color.Transparent), // Necessário para tornar a área da NavBar interativa
                    contentAlignment = Alignment.TopCenter // Ajusta o alinhamento vertical da NavBar
                ) {
                    NavBar()
                }
            }

        }
    }

}