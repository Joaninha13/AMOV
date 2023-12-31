package pt.isec.ans.amov.ui.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.Attraction
import pt.isec.ans.amov.dataStructures.Category
import pt.isec.ans.amov.dataStructures.Location
import pt.isec.ans.amov.ui.Components.Buttons.FilterButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Buttons.FilterField
import pt.isec.ans.amov.ui.Components.Buttons.FilterFields
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SearchDropdownButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Buttons.ToggleFilterOption
import pt.isec.ans.amov.ui.Components.Cards.AttractionCard
import pt.isec.ans.amov.ui.Components.Cards.CategoryCard
import pt.isec.ans.amov.ui.Components.Cards.LocationCard
import pt.isec.ans.amov.ui.Components.Nav.SearchViewModel
import pt.isec.ans.amov.ui.Components.Nav.onSearchTriggered
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainMapScreen(
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel,
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
                navController = navController,
                viewModelFB = viewModelFB,
                viewModelL = viewModelL
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
                        viewModelL = viewModelL,
                        viewModelFB = viewModelFB,
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
fun SearchResultsOverlay(
    onItemClicked: () -> Unit,
    searchViewModel: SearchViewModel,
    navController: NavController,
    viewModelFB: FireBaseViewModel,
    viewModelL: LocationViewModel
) {

    val location = viewModelL.currentLocation.observeAsState()


    var geoPoint by remember { mutableStateOf(
        GeoPoint(
            location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
        )
    ) }

    val currentSearchText = searchViewModel.searchBarState.collectAsState()

    val sortOptions = listOf("Categories Name", "Abc", "Zyx", "Distance")
    var selectedSortCriteria by remember { mutableStateOf("") }

    var selectedFilterCriteria by remember { mutableStateOf(FilterFields()) }
    var liableFilterCriteria by remember { mutableStateOf(FilterFields()) }


    // State for fetched data
    var categories by remember { mutableStateOf<List<String>>(emptyList()) }
    var locations by remember { mutableStateOf<List<String>>(emptyList()) }
    var attractions by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(currentSearchText.value) {
        when {
            currentSearchText.value.equals("attractions", ignoreCase = true) -> {
                viewModelFB.getAllAttractions { fetchedAttractions ->
                    attractions = fetchedAttractions
                }
            }
            currentSearchText.value.equals("locations", ignoreCase = true) -> {
                viewModelFB.getAllLocations { fetchedLocations ->
                    locations = fetchedLocations
                }
            }
            currentSearchText.value.equals("categories", ignoreCase = true) -> {
                viewModelFB.getAllCategories { fetchedCategories ->
                    categories = fetchedCategories
                }
            }
        }
    }


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
                            //TODO: make the filter options lists
                            { SearchDropdownButton(FilterField.CATEGORY, "Category", liableFilterCriteria, listOf("Apple", "Banana", "Cherry")){} },
                            { SearchDropdownButton(FilterField.COUNTRY, "Country", liableFilterCriteria, listOf("Apple", "Banana", "Cherry")){} },
                            { SearchDropdownButton(FilterField.LOCATION, "Location", liableFilterCriteria, listOf("Apple", "Banana", "Cherry")){} },
                            { ToggleFilterOption("Approved", selectedFilterCriteria.approved, liableFilterCriteria) }
                        ),
                        liableFilterCriteria,
                        onFilterApplied = { updatedFilters ->
                            selectedFilterCriteria = updatedFilters
                        }
                    )


                    SortButtonWithPopUp(sortOptions) { selectedOption ->
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

                when {
                    currentSearchText.value.equals("attractions", ignoreCase = true) -> {
                        items(attractions) { attractionName ->
                            Text(text = attractionName, style = TextStyle(color = Color.Transparent,))
                            // Assuming 'locationName' is a unique identifier for the location
                            var attractionDetails by remember { mutableStateOf<Attraction?>(null) }
//                            val filteredAndSortedAttractions = remember(attractions, selectedFilterCriteria, selectedSortCriteria) {
//                                attractions
//                                    .filter { attraction ->
//                                        // Apply all necessary filters based on selectedFilterCriteria
//                                        // For example, if there's a filter for approved attractions:
//                                        selectedFilterCriteria.approved == null || attraction.isApproved == selectedFilterCriteria.approved
//                                    }
//                                    .sortedBy { attraction ->
//                                        // Apply sorting based on selectedSortCriteria
//                                        // For example, if sorting by name:
//                                        when (selectedSortCriteria) {
//                                            "Abc" -> attraction.name
//                                            "Zyx" -> attraction.name.reversed() // Just a simplistic example
//                                            "Distance" -> attraction.distance // Assumes 'distance' is a numeric field
//                                            else -> attraction.name
//                                        }
//                                    }
//                            }

                            // Fetch location details when the item enters composition
                            LaunchedEffect(attractionName) {
                                viewModelFB.getAttractionDetails(
                                    userGeo = geoPoint,
                                    name = attractionName,
                                    onResult = { details ->
                                        attractionDetails = details
                                    }
                                )
                            }

                            attractionDetails?.let { attraction ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                navController.navigate(
                                                    Screen.InfoAttraction.createRoute(
                                                        attraction.name
                                                    )
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AttractionCard(
                                            averageRating = attraction.averageRating,
                                            numRatings = attraction.numReviews,
                                            distanceInKmFromCurrent = attraction.distanceInKmFromCurrent,
                                            imageUrl = attraction.imageUrls[0],
                                            name = attraction.name,
                                            numApproved = attraction.numApproved,
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.End,
                                    ) {
                                        //TODO: add the logic to go to the attraction
                                        RoundIconButton(drawableId = R.drawable.vector)

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                                            horizontalAlignment = Alignment.End,
                                            modifier = Modifier
                                                .height(56.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        //TODO: add the logic to approve the attraction
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.thumbs_up),
                                                    contentDescription = "approve icon",
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier
                                                        .padding(1.dp)
                                                        .width(26.dp)
                                                        .height(26.dp)
                                                )
                                            }

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = attraction.numApproved.toString(),
                                                    style = TextStyle(
                                                        fontSize = 12.sp,
                                                        fontFamily = FontFamily(Font(R.font.inter)),
                                                        fontWeight = FontWeight(600),
                                                        color = BlueSoft,
                                                    )
                                                )
                                                Image(
                                                    painter = painterResource(id = R.drawable.check_circle),
                                                    contentDescription = "approved status",
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier
                                                        .padding(1.dp)
                                                        .width(14.dp)
                                                        .height(14.dp),
                                                    colorFilter = if (attraction.numApproved > 2) ColorFilter.tint(Color(0xFF00B913)) else ColorFilter.tint(Color(0xFFFFB800))
                                                )
                                            }
                                        }

                                    }
                                }

                                Spacer(modifier = Modifier.height(50.dp))
                            }
                        }

                    }
                    currentSearchText.value.equals("locations", ignoreCase = true) -> {


                        items(locations) { locationName ->
                            // Assuming 'locationName' is a unique identifier for the location
                            Text(text = locationName, style = TextStyle(color = Color.Transparent,))
                            var locationDetails by remember { mutableStateOf<Location?>(null) }

                            // Fetch location details when the item enters composition
                            LaunchedEffect(locationName) {
                                viewModelFB.getLocationDetails(
                                    userGeo = geoPoint,
                                    name = locationName,
                                    onResult = { details ->
                                        locationDetails = details
                                    }
                                )
                            }

                            locationDetails?.let { location ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navController.navigate(
                                                    Screen.InfoLocation.createRoute(
                                                        "${location.country}_${location.region}"
                                                    )
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LocationCard(
                                            country = location.country,
                                            region = location.region,
                                            numAttractions = location.numAttractions,
                                            distanceInKmFromCurrent = location.distanceInKmFromCurrent,
                                            description = location.description,
                                            imageUrl = location.imageUrl,
                                            numApproved = location.numApproved,
                                        )
                                    }

                                    // Your RoundIconButton or other UI components
                                    RoundIconButton(drawableId = R.drawable.vector)
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }
                        }
                    }
                    currentSearchText.value.equals("categories", ignoreCase = true) -> {
                        items(categories) { categoryName ->
                            Text(text = categoryName, style = TextStyle(color = Color.Transparent,))

                            var categoryDetails by remember { mutableStateOf<Category?>(null) }

                            // Fetch location details when the item enters composition
                            LaunchedEffect(categoryName) {
                                viewModelFB.getCategoryDetails(
                                    name = categoryName,
                                    userGeo = geoPoint,
                                    onResult = { details ->
                                        categoryDetails = details
                                    }
                                )
                            }

                            categoryDetails?.let { category ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                navController.navigate(
                                                    Screen.InfoCategory.createRoute(
                                                        category.name
                                                    )
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CategoryCard(
                                            name = category.name,
                                            numAttractions = category.numAttractions,
                                            description = category.description,
                                            logoUrl = category.logoUrl,
                                            numApproved = category.numApproved,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }


                /*
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
                */
            }
        })
    }
}

