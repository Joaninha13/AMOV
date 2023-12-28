package pt.isec.ans.amov.ui.Screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pt.isec.ans.amov.ui.Components.MapScreen
import pt.isec.ans.amov.ui.Components.Nav.NavBar
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButton
import pt.isec.ans.amov.ui.Components.Cards.AttractionCard
import pt.isec.ans.amov.ui.Components.Cards.LocationCard
import pt.isec.ans.amov.ui.Components.Nav.ActiveNavBar
import pt.isec.ans.amov.ui.Components.Nav.SearchViewModel
import pt.isec.ans.amov.ui.Components.Nav.onSearchTriggered
import pt.isec.ans.amov.ui.theme.BlueHighlight


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TestMapScreen(viewModel: LocationViewModel) {
    // State for the bottom sheet
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val searchViewModel: SearchViewModel = remember { SearchViewModel() }

    val (buttonToCenterClicked, setButtonToCenterClicked) = remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        scrimColor = Color.Transparent,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            SearchResultsOverlay(
                onItemClicked = {
                    // TODO: Handle item clic
                },
                searchViewModel = searchViewModel
            )
        },
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Page Column
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // MapScreen
                    MapScreen(
                        viewModel = viewModel,
                        buttonToCenterClicked,
                        handleButtonToCenterClicked = { newValue ->
                            setButtonToCenterClicked(newValue)
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                            .zIndex(2f) // Guarantees that the NavBar is above the MapScreen
                            .background(Color.Transparent), // Necessary to make the NavBar area interactive
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.End,
                    ) {
                        NavBar(coroutineScope = coroutineScope, modalBottomSheetState = modalBottomSheetState, searchViewModel = searchViewModel)

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                RoundIconButton(
                                    drawableId = R.drawable.categories,
                                    onClick = {
                                        onSearchTriggered(coroutineScope, modalBottomSheetState)
                                        searchViewModel.onSearchTextChanged("Categories")
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(color = Color(0xFFFFFFFF), shape = CircleShape)
                                )
                                RoundIconButton(
                                    drawableId = R.drawable.locations,
                                    onClick = {
                                        onSearchTriggered(coroutineScope, modalBottomSheetState)
                                        searchViewModel.onSearchTextChanged("Locations")
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .background(color = Color(0xFFFFFFFF), shape = CircleShape)
                                )
                            }

                            RoundIconButton(
                                drawableId = R.drawable.vector,
                                onClick = {
                                    //TODO quando se carrega aqui leva para a localizaçao do user
                                    /*coroutineScope.launch {
                                        setButtonToCenterClicked(true)
                                    }*/
                                    setButtonToCenterClicked(true)
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(color = Color(0xFFFFFFFF), shape = CircleShape)
                            )

                        }
                    }

                }
            }
        }
    }
}

// Dummy composable for search results overlay
@Composable
fun SearchResultsOverlay(onItemClicked: () -> Unit, searchViewModel: SearchViewModel) {

    Column(
        verticalArrangement = Arrangement.spacedBy(35.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(680.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Results",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                        color = BlueHighlight,
                    ),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(25.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.filters),
                        contentDescription = "image description",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                    SortButton(_text = "Sort")
                }
            }

            Image(
                painter = painterResource(id = R.drawable.plus),
                contentDescription = "image description",
                contentScale = ContentScale.Fit
            )
        }

        LazyColumn(


            content = {
                if (searchViewModel.searchText.value.equals("attractions", ignoreCase = true)) {
                    items(80) { index ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Change with the correct listing later
                            AttractionCard(
                                attraction = index.toString(),
                                averageRating = 2.3f,
                                numRatings = 3214,
                                distanceInKmFromCurrent = 32.4f,
                                lastComment = "This is the last comment",
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else if (searchViewModel.searchText.value.equals("locations", ignoreCase = true)
                ) {
                    items(80) { index ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Change with the correct listing later
                            LocationCard(
                                country = "Portugal",
                                region = "Lisbon",
                                numAttractions = 4,
                                distanceInKmFromCurrent = 32.4f,
                                description = "This is the description of the location"
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else if (searchViewModel.searchText.value.equals("categories", ignoreCase = true)) {

                } else if (searchViewModel.searchText.value.equals("all categories", ignoreCase = true)) {

                } else if (searchViewModel.searchText.value.equals("all locations", ignoreCase = true)) {

                } else if (searchViewModel.searchText.value.equals("all attractions", ignoreCase = true)) {

                }
                else {
                    // Depois tem de se fazer com os dados da firestore
                    // val filteredItems = attractions.filter { it.contains(searchText, ignoreCase = true) }

                }
            }
        )
    }

}

@Preview
@Composable
fun SearchResultsOverlayPreview() {
    SearchResultsOverlay(onItemClicked = {} , searchViewModel = SearchViewModel())
}


/*
*                     // Example button to show bottom sheet (replace with your actual trigger)
                    Button(onClick = {
                        coroutineScope.launch {
                            if (modalBottomSheetState.isVisible) {
                                modalBottomSheetState.hide()
                            } else {
                                modalBottomSheetState.show()
                            }
                        }
                    }) {
                        Text("Toggle Bottom Sheet")
                    }*/