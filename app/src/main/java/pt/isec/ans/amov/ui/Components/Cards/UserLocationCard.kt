package pt.isec.ans.amov.ui.Components.Cards

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.DangerRoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft

@Composable
fun UserLocationCard(
    navController: NavController,
    viewModel: FireBaseViewModel,
    region: String,
    country: String,
    numAttractions: Int,
    image : String,

    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
){

    //Card Row
    Row(
        modifier = Modifier
            .width(330.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        //Image column
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(100.dp)
                .width(100.dp)
        ) {
            Image(
                painter = rememberImagePainter(image),
                contentDescription = "image description",
                contentScale = ContentScale.Crop, // Use ContentScale.Crop
                modifier = Modifier.fillMaxSize() // This makes the Image fill the container
            )
        }

        //Row for title and other stuff
        Row(
            modifier = Modifier
                .width(230.dp)
                .height(90.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {

            //Left side of the row
            Column(
                modifier = Modifier
                    .width(164.dp)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {

                //Location Title
                Text(
                    text = "$region, $country",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        fontWeight = FontWeight(600),
                        color = BlueHighlight,
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start)
                ) {

                    //Number of attractions
                    Text(
                        text = "$numAttractions attractions",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = BlueSoft,
                        )
                    )
                }

                //Row with Button GO TO and Delete
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SecButton(_text = "Go to Page", onClick = {navController.navigate(Screen.InfoLocation.createRoute("${country}_$region"))})
                }

            }

            Column(
                modifier = Modifier
                    .width(52.dp)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,

                ) {

                //Icon container
                RoundIconButton(drawableId = R.drawable.edit, onClick = {navController.navigate(Screen.EditLocation.createRoute("${country}_$region"))})
                DangerRoundIconButton(drawableId = R.drawable.trash, onClick = {
                    if (numAttractions > 0) {
                        Toast.makeText(
                            navController.context,
                            "Can't delete location with attractions",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@DangerRoundIconButton
                    }
                    else{
                        viewModel.deleteLocation("${country}_$region")

                        Toast.makeText(navController.context, viewModel.error.value ?: "Location deleted", Toast.LENGTH_SHORT).show()

                        navController.navigate(Screen.ViewLocation.route)
                    }
                })

            }

        }


    }

}

@Preview
@Composable
fun UserLocationCardPreview(){
    UserLocationCard(
        country = "France",
        numAttractions = 4321,
        region = "Paris",
        image = "https://www.planetware.com/photos-large/F/france-paris-eiffel-tower.jpg",
        navController = rememberNavController(),
        viewModel = FireBaseViewModel(),
    )
}
