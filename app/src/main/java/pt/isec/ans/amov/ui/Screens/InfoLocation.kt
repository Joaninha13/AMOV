package pt.isec.ans.amov.ui.Screens

// Correct the imports
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.SortButton
import pt.isec.ans.amov.ui.Components.UserAttractionCard
import pt.isec.ans.amov.ui.theme.*

data class InfoLocationFormState(
    var country: String = "France",
    var region: String = "Paris",
    var description: String = "",
    var coordinates: String = "48°51'52.9776''N 2°20'56.4504''E",


    var numRelatedAttractions: Int = 32,
)


@Preview
@Composable
fun InfoLocation() {
    var locationFormState by remember { mutableStateOf(InfoLocationFormState()) }

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
                        text = locationFormState.country + ", " + locationFormState.region,
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


            Text(
                text = locationFormState.coordinates, // Example of using state
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)), // Ensure this font exists in your res/font folder
                    fontWeight = FontWeight.W500,
                    color = BlueSoft
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .height(214.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.torre_eiffel),
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop, // Use ContentScale.Crop
                    modifier = Modifier.fillMaxSize() // This makes the Image fill the container
                )
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Related Attractions (" + locationFormState.numRelatedAttractions + ")",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        color = BlueHighlight,
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .height(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.filters),
                        contentDescription = "filter-icon",
                        contentScale = ContentScale.None,
                        modifier = Modifier
                            .padding(1.dp)
                            .width(20.dp)
                            .height(20.dp)
                    )

                    SortButton("Sort Attractions")
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
                            UserAttractionCard()
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            )



        }
    }
}