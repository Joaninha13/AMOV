package pt.isec.ans.amov.ui.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.Location
import pt.isec.ans.amov.ui.Components.Buttons.DescriptionButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Cards.AttractionCard
import pt.isec.ans.amov.ui.Components.Buttons.SortButtonWithPopUp
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.*
import kotlin.math.abs


@Composable
fun InfoLocation(
    navController: NavController,
    viewModelFB: FireBaseViewModel,
    viewModelL: LocationViewModel,

    locationId: String
) {

    val location = viewModelL.currentLocation.observeAsState()

    val sortOptions = listOf("Categories Name", "Abc", "Zyx", "Distance")
    var selectedSortCriteria by remember { mutableStateOf("") }


    val geoPoint by remember { mutableStateOf(
        GeoPoint(
            location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
        )
    ) }

    var formState by remember { mutableStateOf<Location?>(null) }

    // Fetch location details when the item enters composition
    LaunchedEffect(locationId) {
        viewModelFB.getLocationDetails(
            userGeo = geoPoint,
            name = locationId,
            onResult = { details ->
                formState = details
            }
        )
    }

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
                    val color = if ((formState?.numApproved ?: 0) >= 2) BlueHighlight else WarningsError

                    Text(
                        text = (formState?.country ?: "Unknown") + ", " + (formState?.region ?: "Unknown"),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            color = color,
                        )
                    )

                    formState?.let {
                        DescriptionButtonWithPopUp(description = it.description, author = it.userRef)
                    }
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


            Text(
                text = getDMSFormattedText(formState?.coordinates?.latitude?.toDouble(), formState?.coordinates?.longitude?.toDouble()),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
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
                    painter = rememberImagePainter(formState?.imageUrl),
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            /*
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Related Attractions (" + formState?.numAttractions + ")",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        color = BlueHighlight,
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    SortButtonWithPopUp(sortOptions) { selectedOption ->
                        selectedSortCriteria = selectedOption
                    }
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
//                            AttractionCard(
//                                attractionId = "1",
//                                navController = navController,
//                                attraction = "Torre Eiffel Tower",
//                                averageRating = 2.3f,
//                                numRatings = 3214,
//                                distanceInKmFromCurrent = 32.4f,
//                                lastComment = "This is the last comment",
//                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            )
            */


        }
    }
}


// Extension function to convert decimal degree to DMS (Degree, Minutes, Seconds)
fun Double.toDMS(): String {
    val degree = this.toInt()
    val minute = ((this - degree) * 60).toInt()
    val second = (((this - degree) * 60 - minute) * 60)
    return String.format("%d°%d′%.1f″", degree, minute, second)
}

// Function to format latitude and longitude into DMS
fun formatCoordinatesToDMS(latitude: Double, longitude: Double): String {
    // Determine the directions
    val latDirection = if (latitude >= 0) "N" else "S"
    val lonDirection = if (longitude >= 0) "E" else "W"

    // Convert decimal degrees to DMS
    val latDMS = abs(latitude).toDMS() + latDirection
    val lonDMS = abs(longitude).toDMS() + lonDirection

    return "$latDMS $lonDMS"
}

// Function to get formatted DMS text for GeoPoint
fun getDMSFormattedText(latitude: Double?, longitude: Double?): String {
    // Replace with actual latitude and longitude or default values
    val lat = latitude ?: 0.0
    val lon = longitude ?: 0.0

    return formatCoordinatesToDMS(lat, lon)
}
