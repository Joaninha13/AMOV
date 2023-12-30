package pt.isec.ans.amov.ui.Components


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberImagePainter
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.Attraction
import pt.isec.ans.amov.ui.Components.Buttons.FilterField
import pt.isec.ans.amov.ui.Components.Buttons.FilterFields
import pt.isec.ans.amov.ui.Components.Buttons.SearchDropdownButton
import pt.isec.ans.amov.ui.Components.PopUps.PopUpBase
import pt.isec.ans.amov.ui.Screens.AttractionFormState
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.BlueLighter
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel,
    buttonToCenterClicked : Boolean,
    handleButtonToCenterClicked : (Boolean) -> Unit
){
    var showPopUp by remember { mutableStateOf(false) }
    var showMarkerPopUp by remember { mutableStateOf(false) }
    var attraction by remember { mutableStateOf<Attraction?>(null) }

    var autoEnabled by remember{ mutableStateOf(false) }
    var attractionGeoPoint by remember { mutableStateOf(GeoPoint(0.0, 0.0)) }
    val location = viewModelL.currentLocation.observeAsState()


    var geoPoint by remember { mutableStateOf(
        GeoPoint(
            location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
        )
    ) }

    Log.d("MapScreen", "Location: ${location.value}")
    Log.d("MapScreen", "GeoPoint: $geoPoint")

    //if (autoEnabled)
        LaunchedEffect(key1 = location.value) {
            geoPoint = GeoPoint(
                location.value?.latitude ?: 0.0, location.value?.longitude ?: 0.0
            )
        }

    Column(
        modifier = Modifier //modifier com m pequeno que é passado por parametro
            .fillMaxSize(),
            //.padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .background(Color(255, 240, 128)),
        ) {
            var mapView by remember { mutableStateOf<MapView?>(null) }

            AndroidView(
                factory = { context ->

                    mapView = MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK);//==TileSourceFactory.DEFAULT_TILE_SOURCE
                        setMultiTouchControls(true)
                        controller.setCenter(geoPoint)
                        controller.setZoom(20.0)
                        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                        viewModelFB.getAllLocationsCoordinates { pois ->
                            for (poi in pois)
                                overlays.add(
                                    Marker(this).apply {
                                        position = GeoPoint(poi.latitude, poi.longitude)
                                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                        icon = ContextCompat.getDrawable(context, R.drawable.location_marker)
                                        title = "${poi.latitude} ${poi.longitude}"
                                    }
                                )

                        }
                        viewModelFB.getAllAttractionsCoordinates { attractionsCords ->
                            for (attractionCords in attractionsCords) {
                                viewModelFB.getAttractionCategory(attractionCords) { category ->

                                    Log.d("Categoria", "$category")
                                    when (category) {
                                        "Alojamentos" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.house)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")

                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        "Jardins" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.park)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")
                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        "Miradouros" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.binoculars)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")
                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        "Monumentos&Locais" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.monu_2)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")
                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        "Museu" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.museum)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")
                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        "parque de diversoes" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.amusement)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")
                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        "Restaurantes&Bares" -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = BitmapDrawable(
                                                        context.resources,
                                                        Bitmap.createScaledBitmap(
                                                            (ContextCompat.getDrawable(context, R.drawable.restaurant)?.toBitmap() ?: return@apply),
                                                            50,
                                                            50,
                                                            false
                                                        )
                                                    )
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true
                                                        Log.d("Click", "cliquei")
                                                        true
                                                    }
                                                }
                                            )
                                        }

                                        else -> {
                                            overlays.add(
                                                Marker(this).apply {
                                                    position = GeoPoint(attractionCords.latitude, attractionCords.longitude)
                                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                                    icon = ContextCompat.getDrawable(context, R.drawable.location_marker)
                                                    title = "${attractionCords.latitude} ${attractionCords.longitude}"
                                                    infoWindow = null

                                                    setOnMarkerClickListener{ _, _ ->
                                                        viewModelFB.getAttractionDetails(attractionCords) { result ->
                                                            attraction = result
                                                        }

                                                        showMarkerPopUp = true

                                                        true
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        overlays.add(
                            MyLocationNewOverlay(this).apply {
                                enableMyLocation()

                                val defaultDrawable =
                                    ContextCompat.getDrawable(context, R.drawable.position)
                                        ?.toBitmap(100, 100, null)

                                setDirectionIcon(defaultDrawable)
                                setPersonIcon(defaultDrawable)
                            }
                        )

                        // Adiciona o MapEventsReceiver diretamente ao MapView -> isto serve para quando a pessoa pressionar drante algum tempo seguido sejam obtidas as coordenadas do ponto onde carregou
                        val mReceive = object : MapEventsReceiver {
                            private val handler = Handler()
                            private val longPressDuration =
                                1500L // Tempo de pressão longa em milissegundos (2 segundos)
                            private var lastPressTime = 0L

                            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                                showPopUp = false
                                return false
                            }

                            override fun longPressHelper(p: GeoPoint?): Boolean {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastPressTime > longPressDuration) {
                                    handler.postDelayed({
                                        p?.let {
                                            showPopUp = true
                                            attractionGeoPoint = GeoPoint(it.latitude, it.longitude)
                                            //Toast.makeText(context, "${it.latitude} - ${it.longitude}", Toast.LENGTH_LONG).show()
                                        }
                                    }, longPressDuration)
                                    lastPressTime = currentTime
                                }

                                return true
                            }

                        }

                        overlays.add(MapEventsOverlay(mReceive))
                    }
                    mapView!!
                },
                update = { view ->
                    view.controller.setCenter(geoPoint)
                    view.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                    if(buttonToCenterClicked){
                        view.controller.animateTo(geoPoint, 20.0, 1500, null)
                        handleButtonToCenterClicked(false)
                    }
                }
            )
        }

        ShowPopUpBase(showPopUp, attractionGeoPoint, LocalContext.current, viewModelFB){
            showPopUp = false
        }

        attraction?.let {
            ShowMarkerPopUp(showMarkerPopUp, it){
                showMarkerPopUp = false
            }
        }
    }
}

@Composable
fun ShowPopUpBase(
    showPopUp: Boolean,
    attractionGeoPoint: GeoPoint,
    context: Context,
    viewModelFB: FireBaseViewModel,
    onDismiss: () -> Unit
) {
    var attractionFormState by remember { mutableStateOf(AttractionFormState()) }

    var inputValues by remember { mutableStateOf(FilterFields()) }

    var categoriesList by remember { mutableStateOf<List<String>>(emptyList()) }

    var locationList by remember { mutableStateOf<List<String>>(emptyList()) }


    viewModelFB.getAllCategories { loadedCategories ->
        categoriesList = loadedCategories
    }

    viewModelFB.getAllLocations { loadedLocations ->
        locationList = loadedLocations
    }

    val pickImagesLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { results ->
            val uris = results.mapNotNull { it }
            attractionFormState.imageUri = uris

        }

    if (showPopUp) {
        PopUpBase(
            showDialog = true,
            title = "Add Attraction",
            content = {
                //First inputs
                Column(
                    modifier = Modifier
                        .width(300.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {

                    //Name
                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        OutlinedInput(
                            _value = attractionFormState.name,
                            _label = "Name",
                            _iconName = R.drawable.nameicon,
                            onValueChange = { newValue ->
                                attractionFormState.name = newValue
                            }
                        )
                    }

                    //Description
                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        OutlinedInput(
                            _value = attractionFormState.description,
                            _label = "Region",
                            _iconName = R.drawable.descicon,
                            onValueChange = { newValue ->
                                attractionFormState.description = newValue
                            }
                        )
                    }

                    //Category
                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(size = 5.dp))
                            .width(300.dp)
                            .height(50.dp)
                            .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp))
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        SearchDropdownButton(FilterField.CATEGORY, "Category", inputValues, categoriesList){ newValue ->
                            attractionFormState.category = newValue
                        }
                    }

                    //Location
                    Row(
                        modifier = Modifier
                            .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(size = 5.dp))
                            .width(300.dp)
                            .height(50.dp)
                            .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp))
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        SearchDropdownButton(FilterField.LOCATION, "Location", inputValues, locationList){newValue ->
                            attractionFormState.location = newValue
                        }
                    }

                    //Upload Images
                    Row(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = BlueLighter,
                                shape = RoundedCornerShape(size = 5.dp)
                            )
                            .width(300.dp)
                            .height(30.dp)
                            .background(
                                color = Color(0xCCFFFFFF),
                                shape = RoundedCornerShape(size = 5.dp)
                            )
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ClickableText(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Blue)) {
                                    append("Upload Image")
                                }
                            },
                            onClick = { offset ->
                                // Iniciar a atividade de escolha de imagem da galeria
                                pickImagesLauncher.launch("image/*")
                            },
                            modifier = Modifier.clickable {
                                // por aqui a foto que deu upload
                            }
                        )
                    }
                }
                      },
            buttonText = "Add",
            onConfirm = {
                attractionFormState.coordinates = com.google.firebase.firestore.GeoPoint(
                    attractionGeoPoint.latitude,
                    attractionGeoPoint.longitude
                )

                var imageUrls: List<String> = listOf()

                attractionFormState.imageUri.let { uri ->
                    // Quando o botão de registro é clicado, faz o upload da imagem
                    viewModelFB.uploadImages(uri) { imageUrl ->
                        attractionFormState.image = imageUrl

                        viewModelFB.addAtraction(
                            attractionFormState.name,
                            attractionFormState.description,
                            attractionFormState.coordinates,
                            attractionFormState.category,
                            attractionFormState.location,
                            attractionFormState.image
                        ) { e ->
                            if (e == null) {
                                Toast.makeText(context, "Attraction added", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            },
            onDismiss = onDismiss
        )
    }
}

@Composable
fun ShowMarkerPopUp(
    showMarkerPopUp: Boolean,
    attraction: Attraction,
    onDismiss: () -> Unit
) {
    if (showMarkerPopUp) {
        Log.d("Click", "entrei aqui")

        PopUpBase(
            showDialog = true,
            title = attraction.name,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Mostra o carrossel de imagens
                    ImageCarousel(images = attraction.imageUrl, modifier = Modifier.fillMaxWidth().height(200.dp))

                    // Exibe outras informações da atração
                    Text(text = "Description: ${attraction.description}", modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "Category: ${attraction.category}", modifier = Modifier.padding(vertical = 8.dp))
                }
            },
            buttonText = "OK",
            onConfirm = onDismiss,
            onDismiss = onDismiss
        )
    }
}

/*@Composable
fun ShowMarkerPopUp(
    showMarkerPopUp: Boolean,
    viewModelFB: FireBaseViewModel,
    attractionGeoPoint: GeoPoint,
    onDismiss: () -> Unit
) {
    var attraction by remember { mutableStateOf<Attraction?>(null) }

    if(showMarkerPopUp){
        PopUpBase(
            showDialog = true,
            title = "oi",
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Mostra o carrossel de imagens
                    //ImageCarousel(images = attractionDetails.imageUrl, modifier = Modifier.fillMaxWidth().height(200.dp))

                    // Exibe outras informações da atração
                    Text(text = "Description:", modifier = Modifier.padding(vertical = 8.dp))
                }
            },
            buttonText = "OK",
            onConfirm = {},
            onDismiss = onDismiss
        )
    }


}*/

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: List<String>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    HorizontalPager(
        state = rememberPagerState(pageCount = { images.size }),
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        val imageUrl = images[page]
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    // Optional: Add image transformations
                    error(Color.Red)
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
    }
}