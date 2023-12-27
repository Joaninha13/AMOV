package pt.isec.ans.amov.ui.Components

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft

@Composable
fun UserLocationCard(
    city: String,
    country: String,
    numAttractions: Int,

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
                painter = painterResource(id = R.drawable.paris_france),
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
                    text = "$city, $country",
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
                            //fontFamily = FontFamily(Font(R.font.inter)),
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
                    SecButton(_text = "Go to Page")
                    DangerRoundIconButton(drawableId = R.drawable.trash)
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
                RoundIconButton(drawableId = R.drawable.vector)
                RoundIconButton(drawableId = R.drawable.edit)

            }

        }


    }

}

@Preview
@Composable
fun UserLocationCardPreview(){
    UserLocationCard(
        city = "Paris",
        country = "France",
        numAttractions = 4321
    )
}