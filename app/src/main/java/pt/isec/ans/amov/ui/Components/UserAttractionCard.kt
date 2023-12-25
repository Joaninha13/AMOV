package pt.isec.ans.amov.ui.Components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft

@Preview
@Composable
fun UserAttractionCard(){

    //Card Row
    Row(
        modifier = Modifier
            .width(320.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp),
            painter = painterResource(id = R.drawable.torre_eiffel),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds
        )

        //Row for title and other stuff
        Row(
            modifier = Modifier
                .width(210.dp)
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
                    text = "Eiffel Tower",
                    style = TextStyle(
                        fontSize = 16.sp,
                        //fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = BlueHighlight,
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start)
                ) {

                    //Attraction rating
                    Text(
                        text = "2,3",
                        style = TextStyle(
                            fontSize = 12.sp,
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
                        text = "(3214)",
                        style = TextStyle(
                            fontSize = 12.sp,
                            //fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = BlueSoft,
                        )
                    )
                }

                //Row with Button GO TO and Delete
                Row {
                    OutlinedButton(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp)),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "Go to Page",
                            style = TextStyle(
                                fontSize = 10.sp,
                                //fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = BlueSoft,
                            )
                        )
                        Log.d("D", "Go to Page")
                    }
                }

            }

            //Right side of the row
            Column(
                modifier = Modifier
                    .width(26.dp)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
            ) {

                //Icon container
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = BlueLighter,
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .size(31.dp)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // Icon
                    Image(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Vector",
                        contentScale = ContentScale.None,
                    )
                }

                //Icon container
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = BlueLighter,
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .size(31.dp)
                        .padding(5.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    // Icon
                    Image(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Edit",
                        contentScale = ContentScale.None
                    )
                }

            }

        }

    }

}