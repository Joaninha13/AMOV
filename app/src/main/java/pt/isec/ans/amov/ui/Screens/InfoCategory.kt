package pt.isec.ans.amov.ui.Screens

// Correct the imports
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.Category
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.*



@Composable
fun InfoCategory(
    navController: NavController,
    viewModelFB: FireBaseViewModel,
    viewModelL: LocationViewModel,
    categoryId: String
) {
    var formState by remember { mutableStateOf<Category?>(null) }

    val sortOptions = listOf("Categories Name", stringResource(R.string.ascendant), stringResource(R.string.descendant), stringResource(R.string.distance))
    var selectedSortCriteria by remember { mutableStateOf("") }

    val location = viewModelL.currentLocation.observeAsState()


    val geoPoint by remember { mutableStateOf(
        GeoPoint(
            location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
        )
    ) }

    // Fetch location details when the item enters composition
    LaunchedEffect(categoryId) {
        viewModelFB.getCategoryDetails(
            name = categoryId,
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
                    //Change with the correct icon later
                    Image(
                        painter = rememberImagePainter(formState?.logoUrl),
                        contentDescription = "image description",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .padding(1.dp)
                            .width(24.dp)
                            .height(24.dp)
                    )
                    formState?.let {
                        val color = if ((formState?.numApproved ?: 0) >= 2) BlueHighlight else WarningsError

                        Text(
                            text = it.name,
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.inter_bold)),
                                color = color,
                            )
                        )
                    }
                    formState?.let {
                        //DescriptionButtonWithPopUp(description = it.description, author = it.userRef)
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
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {

                formState?.let {
                    Text(
                        text = it.description,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = BlueSoft,
                        )
                    )
                }
                Text(
                    text = "Created by ${formState?.userRef?: "Unknown"}",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                        color = BlueSoft,
                        textAlign = TextAlign.Right,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
