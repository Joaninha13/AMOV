package pt.isec.ans.amov.ui.Screens


import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.Attraction
import pt.isec.ans.amov.dataStructures.Location
import pt.isec.ans.amov.dataStructures.Review
import pt.isec.ans.amov.ui.Components.Buttons.DescriptionButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Cards.ReviewCard
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButtonWithPopUp
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.*


@Composable
fun InfoAttraction(
    navController: NavController,
    attractionId: String,
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel,
) {
    val location = viewModelL.currentLocation.observeAsState()

    val sortOptions = listOf("Abc", "Zyx")
    var selectedSortCriteria by remember { mutableStateOf("") }



    val geoPoint by remember { mutableStateOf(
        GeoPoint(
            location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
        )
    ) }

    var formState by remember { mutableStateOf<Attraction?>(null) }

    var reviewsList = remember { mutableStateListOf<Review>() }

    // Fetch location details when the item enters composition
    LaunchedEffect(attractionId) {
        viewModelFB.getAttractionDetails(
            userGeo = geoPoint,
            name = attractionId,
            onResult = { details ->
                formState = details
                details.reviews?.let {
                    reviewsList.clear()
                    reviewsList.addAll(it) // Safely adding reviews to the list
                }
            },
        )
    }

    // State to hold sorted reviews
    val sortedReviewsList = remember { mutableStateListOf<Review>() }

    // Update sorted list whenever the selected criteria changes
    LaunchedEffect(selectedSortCriteria) {
        sortedReviewsList.clear()
        sortedReviewsList.addAll(
            when (selectedSortCriteria) {
                "Abc" -> reviewsList.sortedBy { it.description }
                "Zyx" -> reviewsList.sortedByDescending { it.description }
                else -> reviewsList
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
                        text = attractionId,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            color = color,
                        )
                    )
                    formState?.let {
                        DescriptionButtonWithPopUp(description = it.description, author = it.userRef.toString())
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
                        text = formState?.averageRating.toString(),
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
                        text = "(${formState?.numReviews})",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = BlueSoft,
                        )
                    )
                }
                //TODO: Meter Category(agora esta em referencia, tinhamos de passar para nome)
//                formState?.let {
//                    Text(
//                        text = it.category,
//                        style = TextStyle(
//                            fontSize = 12.sp,
//                            fontFamily = FontFamily(Font(R.font.inter)),
//                            fontWeight = FontWeight(600),
//                            color = BlueSoft,
//                        )
//                    )
//                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
//                Text(
//                    text = "${formState.name}, ${formState.region}",
//                    style = TextStyle(
//                        fontSize = 12.sp,
//                        fontFamily = FontFamily(Font(R.font.inter)),
//                        fontWeight = FontWeight(600),
//                        color = BlueSoft,
//                    )
//                )
                Text(
                    text = getDMSFormattedText(formState?.coordinates?.latitude?.toDouble(), formState?.coordinates?.longitude?.toDouble()),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        color = BlueSoft
                    )
                )
            }



            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                // Main image

                // Horizontal scrollable row for smaller images
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(formState?.imageUrls?.size ?: 0) { index ->
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            when (index % 3) { // Use modulo 3 for repeating pattern every 3 images
                                0 -> {
                                    // First image in the set
                                    Image(
                                        painter = rememberImagePainter(formState?.imageUrls?.get(index)),
                                        contentDescription = "Image $index",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(225.dp, 250.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                    )
                                }
                                1 -> {
                                    // 1 - First small image, align it to the top of the next column
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Image(
                                            painter = rememberImagePainter(formState?.imageUrls?.get(index)),
                                            contentDescription = "Small top image $index",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(120.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                        )
                                        // 2 - Second small image, right below the first one in the same column
                                        if (index + 1 < (formState?.imageUrls?.size ?: 0)) {
                                            Image(
                                                painter = rememberImagePainter(formState?.imageUrls?.get(index + 1)),
                                                contentDescription = "Small bottom image ${index + 1}",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(120.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                            )
                                        }
                                    }
                                }
                            }
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
                    SortButtonWithPopUp(sortOptions) { selectedOption ->
                        selectedSortCriteria = selectedOption
                    }

                    //TODO: Add Review
                    SecButton("Contribute Review")
                }
            }




            if (formState != null) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sortedReviewsList) { review ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            ReviewCard(
                                comment = review.description,
                                rating = review.rating.toInt(),
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }



        }
    }
}

@Preview
@Composable
fun InfoAttractionPreview() {
//    InfoAttraction(
//        navController = NavController(LocalContext.current),
//        attractionId = "Torre Eiffel",
//        viewModelL = LocationViewModel(),
//        viewModelFB = FireBaseViewModel()
//    )
}
