package pt.isec.ans.amov.ui.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import pt.isec.ans.amov.ui.Components.Cards.ReviewCard
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButton
import pt.isec.ans.amov.ui.theme.*

data class InfoAttractionFormState(
    var name: String = "Torre Eiffel",
    var country: String = "France",
    var region: String = "Paris",
    var description: String = "",
    var coordinates: String = "48°51'52.9776''N 2°20'56.4504''E",
    var category: String = "Monument", //will need to change this later

    var averageRating: Float = 2.3f,
    var numRatings: Int = 3214,
)


@Composable
fun InfoAttraction(
    navController: NavController,
) {
    var formState by remember { mutableStateOf(InfoAttractionFormState()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = formState.country + ", " + formState.region,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            color = BlueHighlight,
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "image description",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .padding(1.dp)
                            .width(16.dp)
                            .height(16.dp)
                    )
                }
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

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    //Attraction rating
                    Text(
                        text = formState.averageRating.toString(),
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
                        text = "(${formState.numRatings})",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = BlueSoft,
                        )
                    )
                }
                Text(
                    text = formState.category,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = BlueSoft,
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "${formState.name}, ${formState.region}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = BlueSoft,
                    )
                )
                Text(
                    text = formState.coordinates,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        color = BlueSoft
                    )
                )
            }



//            Row(
//                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
//                verticalAlignment = Alignment.Top,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(16.dp))
//                    .height(214.dp)
//            ) {
//                // Main image
//                Image(
//                    painter = painterResource(id = R.drawable.torre_eiffel),
//                    contentDescription = "Main image",
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .height(225.dp)
//                        .width(225.dp)
//                        .clip(RoundedCornerShape(16.dp))
//                )
//
//                // Horizontal scrollable row for smaller images
//                LazyRow(
//                    // Add padding to allow for the overflow effect of the larger image
//                    contentPadding = PaddingValues(start = 16.dp, top = (-30).dp, end = 16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//
//                    items(10) { index ->
//                        Image(
//                            painter = painterResource(id = R.drawable.torre_eiffel),
//                            contentDescription = "Small image $index",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .size(120.dp)
//                                .clip(RoundedCornerShape(12.dp))
//                        )
//                    }
//                }
//            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                // Main image
                Image(
                    painter = painterResource(id = R.drawable.torre_eiffel),
                    contentDescription = "Main image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(225.dp, 250.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                // Horizontal scrollable row for smaller images
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(5) { index ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Top small image
                            Image(
                                painter = painterResource(id = R.drawable.torre_eiffel),
                                contentDescription = "Small image top $index",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            // Bottom small image
                            Image(
                                painter = painterResource(id = R.drawable.torre_eiffel),
                                contentDescription = "Small image bottom $index",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    SortButton("Sort Attractions")

                    SecButton("Contribute Review")
                }
            }


            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    items(5) { index ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Change with the correct listing later
                            ReviewCard(
                                "“The Eiffel tower is iconic and a must see in Paris. I visited it in 1981 and the surrounding areas have been greatly improved and beautified since then making it a pleasing journey to the tower and a place to stroll around.\u2028\u2028It was a freezing cold day on n this visit so ruff up if you go in the winter, also it pays to our purchase your tickets because like so many other majors Tories attractions it is extremely busy even sat this time of year.\u2028\u2028The views are spectacular and with the changing if the leaves in autumn paid is very beautiful.”",
                                2,
                                index,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            )



        }
    }
}
