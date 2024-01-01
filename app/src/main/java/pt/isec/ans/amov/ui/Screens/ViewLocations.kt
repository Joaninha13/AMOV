package pt.isec.ans.amov.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.LocationDetails
import pt.isec.ans.amov.ui.Components.Cards.UserLocationCard
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight

@Composable
fun ViewLocations(
    navController: NavController,
    viewModel: FireBaseViewModel
) {
    var locationList by remember { mutableStateOf<List<LocationDetails>>(emptyList()) }

    val context = LocalContext.current

    // Buscar categorias quando o Composable for iniciado
    DisposableEffect(viewModel) {
        viewModel.getAllLocationsByUser { location, error ->
            if (error == null) {
                locationList = location
            } else {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        // Cancelar o efeito quando o Composable for removido
        onDispose { }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        //Page Column
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 20.dp, top = 35.dp, end = 20.dp)
        ) {

            //Title + Button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                //Title
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Active Locations",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            color = BlueHighlight,
                        )
                    )
                }

                //Close Button
                Box(
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                ) {
                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(16.dp)
                            .height(16.dp),
                        painter = painterResource(id = R.drawable.cancellationx1),
                        contentDescription = "Cancel",
                        contentScale = ContentScale.None
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(36.dp)
                ) {
                    items(locationList.size) { index ->
                        UserLocationCard(
                            navController = navController,
                            viewModel = viewModel,
                            region = locationList[index].region,
                            country = locationList[index].country,
                            image = locationList[index].image,
                            numAttractions = locationList[index].numAttractions,

                        )
                    }
                }
            }

        }
    }
}
