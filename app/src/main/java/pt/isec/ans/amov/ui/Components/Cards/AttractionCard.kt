package pt.isec.ans.amov.ui.Components.Cards

import android.content.Context
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft


@Composable
fun AttractionCard(
    navController: NavController,
    attractionId: String,

    attraction: String,
    averageRating: Float,
    numRatings: Int,
    distanceInKmFromCurrent: Float,
    lastComment: String,
    //lastCommentPhoto: Int,???


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

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(100.dp)
                .width(100.dp)
        ) {
            Box(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.InfoAttraction.createRoute(attractionId))
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.torre_eiffel),
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }


        Row(
            modifier = Modifier
                .width(230.dp)
                .height(90.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
        ) {


            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .height(32.dp)
                        .width(210.dp)
                ) {
                    //Attraction Title
                    Text(
                        text = attraction,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            fontWeight = FontWeight(600),
                            color = BlueHighlight,
                        )
                    )
                    RoundIconButton(drawableId = R.drawable.vector)
                }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.Start)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        //Attraction rating
                        Text(
                            text = averageRating.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                color = BlueSoft,
                            )
                        )

                        //Star Icon
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "Star icon",
                            contentScale = ContentScale.None
                        )

                        //Number
                        Text(
                            text = "($numRatings)",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                color = BlueSoft,
                            )
                        )
                    }

                    Text(
                        text = "$distanceInKmFromCurrent km",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontWeight = FontWeight(600),
                            color = BlueSoft,
                        )
                    )
                }


                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(214.dp)
                        .height(31.dp)
                ) {
                    RoundIconButton(drawableId = R.drawable.account)
                    Text(
                        text = "''$lastComment''",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = BlueSoft,
                        )
                    )
                }

            }
        }

    }

}

@Preview
@Composable
fun AttractionCardPreview() {
//    AttractionCard(
//        attraction = "Torre Eiffel Tower",
//        averageRating = 2.3f,
//        numRatings = 3214,
//        distanceInKmFromCurrent = 32.4f,
//        lastComment = "This is the last comment ysyayayay sdasyudsadas  kjnasjkd naskjd baskj dbas",
//    )
}