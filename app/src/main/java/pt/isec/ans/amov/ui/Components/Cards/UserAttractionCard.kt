package pt.isec.ans.amov.ui.Components.Cards

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.DangerRoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft
import pt.isec.ans.amov.ui.theme.Red


@Composable
fun UserAttractionCard(
    navController: NavController,
    viewModel: FireBaseViewModel,
    attraction: String,
    averageRating: String,
    numRatings: Int,
    ApprovedsDelete: Int,
    numApproved: Int,
    toDelete: Boolean,
    image: String,

    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
){

    var delete = toDelete

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

                //Attraction Title
                Text(
                    text = attraction,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        fontWeight = FontWeight(600),
                        color = if (numApproved < 3) Red else BlueHighlight,
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start)
                ) {

                    //Attraction rating
                    Text(
                        text = averageRating.toString(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            //fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = BlueSoft,
                        )
                    )

                    //Start Icon
                    Image(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Start icon",
                        contentScale = ContentScale.None
                    )

                    //Number
                    Text(
                        text = "($numRatings)",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
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
                    SecButton(_text = stringResource(R.string.go_to_page), onClick = {navController.navigate(Screen.InfoAttraction.createRoute(attraction))})
                    DangerRoundIconButton(drawableId = R.drawable.trash, onClick = {

                        if (numApproved < 3) {
                            viewModel.deleteAttraction(attraction)

                            Toast.makeText(navController.context, viewModel.error.value ?: navController.context.getString(R.string.attraction_deleted), Toast.LENGTH_SHORT).show()

                            return@DangerRoundIconButton
                        }

                        if (ApprovedsDelete < 3) {
                            if (!delete) {
                                delete = true
                                viewModel.switchAttractionToDelete(attraction) {
                                    Toast.makeText(
                                        navController.context,
                                        navController.context.getString(R.string.delete_attraction_request_sent_after_3_approved_to_delete_the_attraction_will_be_deleted),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@switchAttractionToDelete
                                }
                            }
                            else{
                                Toast.makeText(
                                    navController.context,
                                    navController.context.getString(R.string.can_t_delete_attractions_not_approvedyet),
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@DangerRoundIconButton
                            }
                        }
                        else{
                            viewModel.deleteAttraction(attraction)

                            Toast.makeText(navController.context, viewModel.error.value ?: navController.context.getText(R.string.attraction_deleted), Toast.LENGTH_SHORT).show()

                        }
                    })
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
                RoundIconButton(drawableId = R.drawable.vector, onClick = onClick)// por isto para ir para as coordenadas da localização no mapa
                RoundIconButton(drawableId = R.drawable.edit, onClick = {navController.navigate(Screen.EditAttraction.createRoute(attraction))})

            }

        }

    }

}

@Preview
@Composable
fun UserAttractionCardPreview() {
    /*UserAttractionCard(
        attraction = "Torre Eiffel Tower",
        averageRating = 2.3f,
        numRatings = 3214
    )*/
}