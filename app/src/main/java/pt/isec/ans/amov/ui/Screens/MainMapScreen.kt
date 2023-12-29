package pt.isec.ans.amov.ui.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.FilterButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Buttons.FilterField
import pt.isec.ans.amov.ui.Components.Buttons.FilterFields
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SearchDropdownButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Buttons.ToggleFilterOption
import pt.isec.ans.amov.ui.Components.Cards.AttractionCard
import pt.isec.ans.amov.ui.Components.Cards.LocationCard
import pt.isec.ans.amov.ui.Components.Nav.SearchViewModel
import pt.isec.ans.amov.ui.Components.Nav.onSearchTriggered
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.theme.BlueHighlight


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainMapScreen(
    locationViewModel: LocationViewModel,
    navController: NavController
) {

    // State for the bottom sheet
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val searchViewModel: SearchViewModel = viewModel() // Scoped to the nearest LifecycleOwner

    val (buttonToCenterClicked, setButtonToCenterClicked) = remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        scrimColor = Color.Transparent,
        sheetBackgroundColor = Color.Transparent,
        sheetContent = {
            SearchResultsOverlay(
                onItemClicked = {
                    // TODO: Handle item click
                },
                searchViewModel = searchViewModel,
                navController = navController
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
                        viewModel = locationViewModel,
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
                        NavBar(
                            coroutineScope = coroutineScope,
                            modalBottomSheetState = modalBottomSheetState,
                            searchViewModel = searchViewModel,
                            navController = navController
                        )

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
                                        searchViewModel.setSearchBarState("Categories")
                                        onSearchTriggered(coroutineScope, modalBottomSheetState)
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .border(2.dp, Color.LightGray, CircleShape)
                                        .background(color = Color(0xFFFFFFFF), shape = CircleShape)
                                )
                                RoundIconButton(
                                    drawableId = R.drawable.locations,
                                    onClick = {
                                        searchViewModel.setSearchBarState("Locations")
                                        onSearchTriggered(coroutineScope, modalBottomSheetState)
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                        .border(2.dp, Color.LightGray, CircleShape)
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
                                    .border(2.dp, Color.LightGray, CircleShape)
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
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchResultsOverlay(onItemClicked: () -> Unit, searchViewModel: SearchViewModel, navController: NavController) {

    val currentSearchText = searchViewModel.searchBarState.collectAsState()

    val sortOptions = listOf("Option 1", "Option 2", "Option 3") // Dummy sort options
    var selectedSortCriteria by remember { mutableStateOf("") }

    var selectedFilterCriteria by remember { mutableStateOf(FilterFields()) }
    var liableFilterCriteria by remember { mutableStateOf(FilterFields()) }

//    List<Map<DomainEntity, Int>> items
//    val filteredItems = items.filter { it.contains(searchText, ignoreCase = true) }

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
                    FilterButtonWithPopUp(
                        filterOptions = listOf(
                            { ToggleFilterOption("Approved", selectedFilterCriteria.approved, liableFilterCriteria) },
                            { SearchDropdownButton(FilterField.CATEGORY, "Category", liableFilterCriteria, listOf("Apple", "Banana", "Cherry")) }
                            // ... other filter options ...
                        ),
                        liableFilterCriteria,
                        onFilterApplied = { updatedFilters ->
                            selectedFilterCriteria = updatedFilters
                        }
                    )


                    SortButtonWithPopUp(sortOptions) { selectedOption ->
                        // Update the selected sort criteria here
                        selectedSortCriteria = selectedOption
                    }
                }
            }

            Box(
                modifier = Modifier.clickable {
                    // Check if the searchText.value equals "attractions" ignoring the case
                    if (currentSearchText.value.equals("attractions", ignoreCase = true)) {
                        navController.navigate(Screen.AddAttractions.route)
                    } else if (currentSearchText.value.equals("locations", ignoreCase = true)) {
                        navController.navigate(Screen.AddLocations.route)
                    } else if (currentSearchText.value.equals("categories", ignoreCase = true)) {
                        navController.navigate(Screen.AddCategories.route)
                    }
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "image description",
                    contentScale = ContentScale.Fit
                )
            }

        }

        LazyColumn(


            content = {
                if (currentSearchText.value.equals("attractions", ignoreCase = true)) {
                    items(80) { index ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Change with the correct listing later
                            AttractionCard(
                                navController = navController,
                                attractionId = index.toString(),
                                attraction = index.toString(),
                                averageRating = 2.3f,
                                numRatings = 3214,
                                distanceInKmFromCurrent = 32.4f,
                                lastComment = "This is the last comment",
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                } else if (currentSearchText.value.equals("locations", ignoreCase = true)
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
                } else if (currentSearchText.value.equals("categories", ignoreCase = true)) {

                } else if (currentSearchText.value.equals("all categories", ignoreCase = true)) {

                } else if (currentSearchText.value.equals("all locations", ignoreCase = true)) {

                } else if (currentSearchText.value.equals("all attractions", ignoreCase = true)) {

                }
                else {
                    // Depois tem de se fazer com os dados da firestore
                    // val filteredItems = attractions.filter { it.contains(searchText, ignoreCase = true) }
                    items(80) { index ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Change with the correct listing later
                            LocationCard(
                                country = currentSearchText.value,
                                region = "Lisbon",
                                numAttractions = 4,
                                distanceInKmFromCurrent = 32.4f,
                                description = "This is the description of the location"
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        )
    }

}

@Preview
@Composable
fun SearchResultsOverlayPreview() {
    SearchResultsOverlay(onItemClicked = {} , searchViewModel = SearchViewModel(), navController = NavController(LocalContext.current))
}
