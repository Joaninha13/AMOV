package pt.isec.ans.amov.ui.Components


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel,
    buttonToCenterClicked : Boolean,
    handleButtonToCenterClicked : (Boolean) -> Unit
){

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
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
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
                    }
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
        /*Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(viewModel.POIs) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(128,192,255)
                    ),
                    onClick = {
                        TODO("...")
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = it.team, fontSize = 20.sp)
                        Text(text = "${it.latitude} ${it.longitude}", fontSize = 14.sp)
                    }
                }
            }
        }*/
    }
}