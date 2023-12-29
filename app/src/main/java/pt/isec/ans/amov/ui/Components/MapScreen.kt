package pt.isec.ans.amov.ui.Components


import android.app.Activity
import android.content.Intent
import android.gesture.GestureOverlayView
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.PopUps.PopUpBase
import pt.isec.ans.amov.ui.Screens.LocationFormState
import pt.isec.ans.amov.ui.Screens.TextInputs
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.BlueLighter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel,
    buttonToCenterClicked : Boolean,
    handleButtonToCenterClicked : (Boolean) -> Unit
){
    var showPopUp by remember { mutableStateOf(false) }
    var autoEnabled by remember{ mutableStateOf(false) }
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
//        Row(
//            modifier= Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "Lat: ${location.value?.latitude ?: "--"}")
//            Switch(checked = autoEnabled, onCheckedChange = {
//                autoEnabled = it
//            })
//            Text(text = "Lon: ${location.value?.longitude ?: "--"}")
//        }
//        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                //.padding(8.dp)
                /*.fillMaxWidth()
                .fillMaxHeight(0.5f)*/
                .fillMaxSize()
                .clipToBounds()
                .background(Color(255, 240, 128)),
        ) {
            var mapView by remember { mutableStateOf<MapView?>(null) }

            AndroidView(
                factory = { context ->

                    /*val mReceive = object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            return false
                        }

                        override fun longPressHelper(p: GeoPoint?): Boolean {
                            p?.let {
                                Toast.makeText(context, "${p.latitude} - ${p.longitude}", Toast.LENGTH_LONG).show()
                            }
                            return true  // Retorna true para indicar que o evento foi tratado
                        }
                    }*/

                    mapView = MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK);//==TileSourceFactory.DEFAULT_TILE_SOURCE
                        setMultiTouchControls(true)
                        controller.setCenter(geoPoint)
                        controller.setZoom(20.0)
                        //controller.setZoom(8.0)
                        viewModelFB.getAllLocationsCoordinates { pois ->
                            for (poi in pois)
                                overlays.add(
                                    Marker(this).apply {
                                        position = GeoPoint(poi.latitude, poi.longitude)
                                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                        icon = ContextCompat.getDrawable(context, R.drawable.location_marker)
                                        //title = poi.team
                                    }
                                )

                        }
                        overlays.add(
                            MyLocationNewOverlay(this).apply {
                                enableMyLocation()
                                //onLocationChanged(null, null)
                                //enableFollowLocation()

                                val defaultDrawable =
                                    ContextCompat.getDrawable(context, R.drawable.position)
                                        ?.toBitmap(100, 100, null)

                                setDirectionIcon(defaultDrawable)
                                setPersonIcon(defaultDrawable)
                            }
                        )

                        /*val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

                            override fun onDown(e: MotionEvent): Boolean {
                                return e.pointerCount == 1
                            }

                            override fun onLongPress(e: MotionEvent){
                                if (e.pointerCount == 1) {
                                    val x = e.x
                                    val y = e.y
                                    val touchedGeoPoint = mapView?.projection?.fromPixels(x.toInt(), y.toInt())
                                    // Lógica adicional, se necessário, ao detectar uma pressão longa
                                    touchedGeoPoint?.let {
                                        Toast.makeText(context, "${it.latitude} - ${it.longitude}", Toast.LENGTH_LONG).show()
                                    }

                                    // Chama performClick para lidar com acessibilidade
                                    performClick()
                                }
                            }
                        })*/

                        // Adiciona o MapEventsReceiver diretamente ao MapView -> isto serve para quando a pessoa pressionar drante algum tempo seguido sejam obtidas as coordenadas do ponto onde carregou
                        val mReceive = object : MapEventsReceiver {
                            private val handler = Handler()
                            private val longPressDuration = 2000L // Tempo de pressão longa em milissegundos (2 segundos)
                            private var lastPressTime = 0L

                            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                                return false
                            }

                            override fun longPressHelper(p: GeoPoint?): Boolean {
                                val currentTime = System.currentTimeMillis()
                                if (currentTime - lastPressTime > longPressDuration) {
                                    handler.postDelayed({
                                        p?.let {
                                            showPopUp = true
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

                    if(buttonToCenterClicked){
                        view.controller.animateTo(geoPoint, 15.0, 1500, null)
                        handleButtonToCenterClicked(false)
                    }
                }
            )
        }

        ShowPopUpBase(showPopUp){
            showPopUp = false
        }
    }
}

@Composable
fun ShowPopUpBase(showPopUp: Boolean, onDismiss: () -> Unit) {
    var locationFormState by remember { mutableStateOf(LocationFormState()) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Log.d("VERRR------>", "result: $result") // passa aqui
                result.data?.data?.let { uri ->
                    // Aqui você tem a URI da imagem selecionada
                    // Agora você pode fazer o upload para o Firestore ou atualizar o estado conforme necessário
                    locationFormState.imageUri = uri
                }
            }
        }

    if (showPopUp) {
        PopUpBase(
            showDialog = true,
            title = "Add Location",
            content = {
                //First inputs
                Column(
                    modifier = Modifier
                        .width(300.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {

                    //Country
                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        OutlinedInput(
                            _value = locationFormState.country,
                            _label = "Country",
                            _iconName = R.drawable.nameicon,
                            onValueChange = { newValue ->
                                locationFormState.country = newValue
                            }
                        )
                    }

                    //Region
                    Row(
                        modifier = Modifier
                            .width(300.dp)
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        OutlinedInput(
                            _value = locationFormState.region,
                            _label = "Region",
                            _iconName = R.drawable.nameicon,
                            onValueChange = { newValue ->
                                locationFormState.region = newValue
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
                            _value = locationFormState.description,
                            _label = "Description",
                            _iconName = R.drawable.descicon,
                            onValueChange = { newValue ->
                                locationFormState.description = newValue
                            }
                        )

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
                            pickImageLauncher.launch(
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            )
                        },
                        modifier = Modifier.clickable {
                            // por aqui a foto que deu upload
                        }
                    )

                    /*Text(
                        text = "Upload Images",
                        style = TextStyle(
                            fontSize = 16.sp,
                            //fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = BlueSoft,
                        )
                    )*/
                }
                      },
            buttonText = "Add",
            onConfirm = {
                // Implemente a lógica de confirmação
            },
            onDismiss = onDismiss
        )
    }
}
