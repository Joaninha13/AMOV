package pt.isec.ans.amov.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration.getInstance
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import pt.isec.ans.amov.Application
import pt.isec.ans.amov.ui.Screens.AddAttraction
import pt.isec.ans.amov.ui.Screens.AddCategory
import pt.isec.ans.amov.ui.Screens.AddLocation
import pt.isec.ans.amov.ui.Screens.MainMapScreen
import pt.isec.ans.amov.ui.Screens.TestMapScreen
import pt.isec.ans.amov.ui.Screens.LoginScreen
import pt.isec.ans.amov.ui.Screens.RegisterAcc
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModelFactory
import pt.isec.ans.amov.ui.theme.ComposeTheme
import pt.isec.ans.amov.ui.theme.LocationMapsTheme

class MainActivity : ComponentActivity() {

    val app by lazy { application as Application }

    //sera aqui??
    private val viewModelL : LocationViewModel by viewModels{ LocationViewModelFactory(app.locationHandler) }
    private val viewModelFB : FireBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //val navController = rememberNavController()
            /*ComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "LoginScreen") {
                        composable("LoginScreen") {
                            LoginScreen(viewModel = viewModelFB) {
                                navController.navigate("AddAttraction")
                            }
                        }
                        composable("AddAttraction") {
                            /*AddAttraction() {
                                navController.navigateUp()
                                // se tiver mais que uma janela de navegação, pode ser necessário usar o popBackStack("LoginScreen")
                            }*/
                        }
                    }
                }
            }*/
            //RegisterAcc(viewModel = viewModelFB) {}
            //LoginScreen(viewModel = viewModelFB) {}
            //AddCategory(viewModel = viewModelFB)
            //AddLocation(ViewModelL = viewModelL, viewModelFB = viewModelFB)
            AddAttraction(viewModelL = viewModelL, viewModelFB = viewModelFB)

        }

        verifyPermissions()
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||

            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||

            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        )

            verifyMultiplePermissions.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
        ) {
            verifyOnePermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModelL.startLocationUpdates()
    }

    val verifyMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {

    }

    val verifyOnePermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {

    }
    private fun verifyPermissions() : Boolean{
        viewModelL.coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        viewModelL.fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModelL.backgroundLocationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else
            viewModelL.backgroundLocationPermission = viewModelL.coarseLocationPermission || viewModelL.fineLocationPermission

        if (!viewModelL.coarseLocationPermission && !viewModelL.fineLocationPermission) {
            basicPermissionsAuthorization.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return false
        } else
            verifyBackgroundPermission()
        return true
    }

    private val basicPermissionsAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        viewModelL.coarseLocationPermission = results[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        viewModelL.fineLocationPermission = results[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        viewModelL.startLocationUpdates()
        verifyBackgroundPermission()
    }

    private fun verifyBackgroundPermission() {
        if (!(viewModelL.coarseLocationPermission || viewModelL.fineLocationPermission))
            return

        if (!viewModelL.backgroundLocationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                val dlg = AlertDialog.Builder(this)
                    .setTitle("Background Location")
                    .setMessage(
                        "This application needs your permission to use location while in the background.\n" +
                                "Please choose the correct option in the following screen" +
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                    " (\"${packageManager.backgroundPermissionOptionLabel}\")."
                                else
                                    "."
                    )
                    .setPositiveButton("Ok") { _, _ ->
                        backgroundPermissionAuthorization.launch(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                    .create()
                dlg.show()
            }
        }
    }

    private val backgroundPermissionAuthorization = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        viewModelL.backgroundLocationPermission = result
        Toast.makeText(this,"Background location enabled: $result", Toast.LENGTH_LONG).show()
    }

}