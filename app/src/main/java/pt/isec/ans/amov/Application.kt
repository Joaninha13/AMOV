package pt.isec.ans.amov

import android.app.Application
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.location.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.location.LocationHandler

class Application : Application() {

    /*val locationHandler : LocationHandler by lazy {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        LocationManagerHandler(locationManager)
    }*/

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
