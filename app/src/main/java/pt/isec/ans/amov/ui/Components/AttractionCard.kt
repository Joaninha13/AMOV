package pt.isec.ans.amov.ui.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft
import pt.isec.ans.amov.ui.theme.WarningsError


@Composable
fun AttractionCard(
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
            Image(
                painter = painterResource(id = R.drawable.torre_eiffel),
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
                            fontFamily = FontFamily(Font(R.font.inter_semibold)),
                            fontWeight = FontWeight(600),
                            color = BlueHighlight,
                        )
                    )
                    RoundIconButton(drawableId = R.drawable.vector)
                }


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
                    modifier = Modifier
                        .width(214.dp)
                        .height(31.dp)
                ) {
                    RoundIconButton(drawableId = R.drawable.account)
                    Text(
                        text = "“The Eiffel tower is iconic and a must see in Paris. I visited it in 1981 and the surrounding areas have been greatly improved and beautified since then making it a pleasing journey to the tower and a place to stroll around.  It was a freezing cold day on n this visit so ruff up if you go in the winter, also it pays to our purchase your tickets because like so many other majors Tories attractions it is extremely busy even sat this time of year.  The views are spectacular and with the changing if the leaves in autumn paid is very beautiful.”\n",
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
    AttractionCard(
        attraction = "Torre Eiffel Tower",
        averageRating = 2.3f,
        numRatings = 3214,
        distanceInKmFromCurrent = 32.4f,
        lastComment = "This is the last comment",
    )
}