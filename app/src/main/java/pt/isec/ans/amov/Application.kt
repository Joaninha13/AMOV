package pt.isec.ans.amov

import android.app.Application
import com.google.android.gms.location.LocationServices
import pt.isec.ans.locationmaps.utils.location.FusedLocationHandler
import pt.isec.ans.locationmaps.utils.location.LocationHandler

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModelFactory
import pt.isec.ans.amov.ui.theme.ComposeTheme

class Application : Application() {

    val locationHandler: LocationHandler by lazy {
        val locationProvider = LocationServices.getFusedLocationProviderClient(this)
        FusedLocationHandler(locationProvider)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
