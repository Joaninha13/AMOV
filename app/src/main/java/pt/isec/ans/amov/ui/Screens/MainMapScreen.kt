package pt.isec.ans.amov.ui.Screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.osmdroid.util.GeoPoint

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
import pt.isec.ans.amov.ui.Components.Nav.SearchResultItem
import pt.isec.ans.amov.ui.Components.Nav.SearchViewModel
import pt.isec.ans.amov.ui.Components.Nav.onSearchTriggered
import pt.isec.ans.amov.ui.Components.toFirebaseGeoPoint
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
    val (buttonToAttractionClicked, setButtonToAttractionClicked) = remember { mutableStateOf(false) }
    val (attractionGeoPointClicked, setAttractionGeoPointClicked) = remember { mutableStateOf<GeoPoint>(
        GeoPoint(0.0, 0.0)
    ) }
    val (buttonToLocationClicked, setButtonToLocationClicked) = remember { mutableStateOf(false) }
    val (locationGeoPointClicked, setLocationGeoPointClicked) = remember { mutableStateOf<GeoPoint>(
        GeoPoint(0.0, 0.0)
    ) }


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
                viewModelL = viewModelL,
                handleButtonToAttractionClicked = { newValue ->
                    setButtonToAttractionClicked(newValue)
                },
                handleAttractionClicked = { newValue ->
                    setAttractionGeoPointClicked(newValue)
                },
                handleButtonToLocationClicked = { newValue ->
                    setButtonToLocationClicked(newValue)
                },
                handleLocationClicked = { newValue ->
                    setLocationGeoPointClicked(newValue)
                }
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
                        },
                        buttonToAttractionClicked,
                        handleButtonToAttractionClicked = { newValue ->
                            setButtonToAttractionClicked(newValue)
                        },
                        attractionGeoPointClicked,
                        buttonToLocationClicked,
                        handleButtonToLocationClicked = { newValue ->
                            setButtonToLocationClicked(newValue)
                        },
                        locationGeoPointClicked
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
    viewModelL: LocationViewModel,
    handleButtonToAttractionClicked : (Boolean) -> Unit,
    handleAttractionClicked : (GeoPoint) -> Unit,
    handleButtonToLocationClicked : (Boolean) -> Unit,
    handleLocationClicked : (GeoPoint) -> Unit
) {

    val location = viewModelL.currentLocation.observeAsState()


    var geoPoint by remember { mutableStateOf(
        GeoPoint(
            location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
        )
    ) }

    val currentSearchText = searchViewModel.searchBarState.collectAsState()

    val sortOptions = listOf("Categories Name",  stringResource(R.string.ascendant),  stringResource(R.string.descendant))
    var selectedSortCriteria by remember { mutableStateOf("") }

    var selectedFilterCriteria by remember { mutableStateOf(FilterFields()) }
    var liableFilterCriteria by remember { mutableStateOf(FilterFields()) }


    // State for fetched data
    var locationNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var categoryNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var attractionNames by remember { mutableStateOf<List<String>>(emptyList()) }


    LaunchedEffect(currentSearchText.value) {
        viewModelFB.getAllAttractions { fetchedAttractions ->
            attractionNames = fetchedAttractions
        }
        viewModelFB.getAllLocations { fetchedLocations ->
            locationNames = fetchedLocations
        }
        viewModelFB.getAllCategories { fetchedCategories ->
            categoryNames = fetchedCategories
        }
    }


    var listOfLocations by remember { mutableStateOf<List<Location>>(emptyList()) }
    var listOfAttractions by remember { mutableStateOf<List<Attraction>>(emptyList()) }
    var listOfCategories by remember { mutableStateOf<List<Category>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }


    LaunchedEffect(categoryNames) {
        if (categoryNames.isNotEmpty()) {
            isLoading = true
            val fetchedCategories = mutableListOf<Category>()
            val totalNames = categoryNames.size
            var loadedCount = 0

            categoryNames.forEach { categoryName ->
                viewModelFB.getCategoryDetails(
                    onResult = { category ->
                        fetchedCategories.add(category)
                        loadedCount += 1
                        if (loadedCount == totalNames) {  // Check if all details are fetched
                            listOfCategories = fetchedCategories
                            isLoading = false
                        }
                    },
                    name = categoryName,
                    userGeo = geoPoint.toFirebaseGeoPoint()
                )
            }
        }
    }

    LaunchedEffect(locationNames) {
        if (locationNames.isNotEmpty()) {
            isLoading = true
            val fetchedLocations = mutableListOf<Location>()
            val totalNames = locationNames.size
            var loadedCount = 0

            locationNames.forEach { locationName ->
                viewModelFB.getLocationDetails(
                    onResult = { location ->
                        fetchedLocations.add(location)
                        loadedCount += 1
                        if (loadedCount == totalNames) {  // Check if all details are fetched
                            listOfLocations = fetchedLocations
                            isLoading = false  // Update isLoading here if separate for locations
                        }
                    },
                    name = locationName,
                    userGeo = geoPoint.toFirebaseGeoPoint()
                )
            }
        }
    }

    LaunchedEffect(attractionNames) {
        if (attractionNames.isNotEmpty()) {
            isLoading = true
            val fetchedAttractions = mutableListOf<Attraction>()
            val totalNames = attractionNames.size
            var loadedCount = 0

            attractionNames.forEach { attractionName ->
                viewModelFB.getAttractionDetails(
                    onResult = { attraction ->
                        fetchedAttractions.add(attraction)
                        loadedCount += 1
                        if (loadedCount == totalNames) {  // Check if all details are fetched
                            listOfAttractions = fetchedAttractions
                            isLoading = false  // Update isLoading here if separate for attractions
                        }
                    },
                    name = attractionName,
                    userGeo = geoPoint.toFirebaseGeoPoint()
                )
            }
        }
    }


    val combinedList by remember(listOfAttractions, listOfLocations, listOfCategories) {
        derivedStateOf {
            mutableListOf<SearchResultItem>().apply {
                addAll(listOfAttractions.map { SearchResultItem.AttractionItem(it) })
                addAll(listOfLocations.map { SearchResultItem.LocationItem(it) })
                addAll(listOfCategories.map { SearchResultItem.CategoryItem(it) })
            }.shuffled()
        }
    }


    val customCombinedList by remember(combinedList, selectedSortCriteria) {
        derivedStateOf {
            when (selectedSortCriteria) {
                "Categories Name" -> combinedList.sortedBy {
                    when (it) {
                        is SearchResultItem.CategoryItem -> it.category.name
                        else -> ""
                    }
                }
                navController.context.getString(R.string.ascendant) -> combinedList.sortedBy {
                    when (it) {
                        is SearchResultItem.AttractionItem -> it.attraction.name
                        is SearchResultItem.LocationItem -> it.location.region
                        is SearchResultItem.CategoryItem -> it.category.name
                    }
                }
                navController.context.getString(R.string.descendant) -> combinedList.sortedByDescending {
                    when (it) {
                        is SearchResultItem.AttractionItem -> it.attraction.name
                        is SearchResultItem.LocationItem -> it.location.region
                        is SearchResultItem.CategoryItem -> it.category.name
                    }
                }

                else -> combinedList
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
                    text = stringResource(R.string.results),
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
                            { SearchDropdownButton(FilterField.LOCATION, "Location", liableFilterCriteria, locationNames) {} },
                            { SearchDropdownButton(FilterField.CATEGORY, "Category", liableFilterCriteria, locationNames){} },
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
                        items(listOfAttractions) { attraction ->

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
                                        RoundIconButton(
                                            drawableId = R.drawable.vector,
                                            onClick = {
                                                handleAttractionClicked(GeoPoint(attraction.coordinates.latitude, attraction.coordinates.longitude))
                                                handleButtonToAttractionClicked(true)
                                            }
                                        )
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
                                                        viewModelFB.addApprovedAttraction(attraction.name) {
                                                            Toast
                                                                .makeText(
                                                                    navController.context,
                                                                    viewModelFB.error.value,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
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
                    currentSearchText.value.equals("locations", ignoreCase = true) -> {

                        items(listOfLocations) { location ->

                                Row(
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Box(
                                        modifier = Modifier
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
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.End,
                                    ) {
                                        // Your RoundIconButton or other UI components
                                        RoundIconButton(
                                            drawableId = R.drawable.vector,
                                            onClick = {
                                                Log.d("DDD", location.coordinates.toString())
                                                handleLocationClicked(GeoPoint(location.coordinates.latitude, location.coordinates.longitude))
                                                handleButtonToLocationClicked(true)
                                            }
                                        )
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
                                                        viewModelFB.addApprovedLocations("${location.country}_${location.region}") {
                                                            Toast
                                                                .makeText(
                                                                    navController.context,
                                                                    viewModelFB.error.value,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
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
                                                horizontalArrangement = Arrangement.spacedBy(
                                                    5.dp,
                                                    Alignment.CenterHorizontally
                                                ),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Text(
                                                    text = location.numApproved.toString(),
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
                                                    colorFilter = if (location.numApproved >= 3) ColorFilter.tint(Color(0xFF00B913)) else ColorFilter.tint(Color(0xFFFFB800))
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                            }

                    }
                    currentSearchText.value.equals("categories", ignoreCase = true) -> {
                        items(listOfCategories) { category ->

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Box(
                                        modifier = Modifier
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
                                    Column(
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.End,
                                    ) {

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
                                                        viewModelFB.addApprovedCategories(category.name) {
                                                            Toast
                                                                .makeText(
                                                                    navController.context,
                                                                    viewModelFB.error.value,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
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
                                                    text = category.numApproved.toString(),
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
                                                    colorFilter = if (category.numApproved >= 3) ColorFilter.tint(Color(0xFF00B913)) else ColorFilter.tint(Color(0xFFFFB800))
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                        }
                    }
                    else -> {
                        items(customCombinedList) { item ->
                        when {
                            item is SearchResultItem.AttractionItem && item.attraction.name.contains(currentSearchText.value, ignoreCase = true) -> {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                navController.navigate(
                                                    Screen.InfoAttraction.createRoute(
                                                        item.attraction.name
                                                    )
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AttractionCard(
                                            averageRating = item.attraction.averageRating,
                                            numRatings = item.attraction.numReviews,
                                            distanceInKmFromCurrent = item.attraction.distanceInKmFromCurrent,
                                            imageUrl = item.attraction.imageUrls[0],
                                            name = item.attraction.name,
                                            numApproved = item.attraction.numApproved,
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
                                                        viewModelFB.addApprovedAttraction(item.attraction.name) {
                                                            Toast
                                                                .makeText(
                                                                    navController.context,
                                                                    viewModelFB.error.value,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                .show()
                                                        }
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
                                                    text = item.attraction.numApproved.toString(),
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
                                                    colorFilter = if (item.attraction.numApproved > 2) ColorFilter.tint(Color(0xFF00B913)) else ColorFilter.tint(Color(0xFFFFB800))
                                                )
                                            }
                                        }

                                    }
                                }

                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            item is SearchResultItem.LocationItem && (item.location.country.contains(currentSearchText.value, ignoreCase = true) || item.location.region.contains(currentSearchText.value, ignoreCase = true)) -> {

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
                                                        "${item.location.country}_${item.location.region}"
                                                    )
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LocationCard(
                                            country = item.location.country,
                                            region = item.location.region,
                                            numAttractions = item.location.numAttractions,
                                            distanceInKmFromCurrent = item.location.distanceInKmFromCurrent,
                                            description = item.location.description,
                                            imageUrl = item.location.imageUrl,
                                            numApproved = item.location.numApproved,
                                        )
                                    }

                                    // Your RoundIconButton or other UI components
                                    RoundIconButton(drawableId = R.drawable.vector)
                                }

                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            item is SearchResultItem.CategoryItem && item.category.name.contains(currentSearchText.value, ignoreCase = true) -> {

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
                                                        item.category.name
                                                    )
                                                )
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CategoryCard(
                                            name = item.category.name,
                                            numAttractions = item.category.numAttractions,
                                            description = item.category.description,
                                            logoUrl = item.category.logoUrl,
                                            numApproved = item.category.numApproved,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        }
                    }

                }
            }
        })
    }
}




