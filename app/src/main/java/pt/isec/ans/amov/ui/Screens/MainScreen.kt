package pt.isec.ans.amov.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


/*enum class Screens(val display: String, val showAppBar : Boolean) {
    MENU("Menu", false),
    SOLID("Solid", true),
    GALLERY("Gallery", true),
    CAMERA("Camera", true),
    LIST("List", true),
    NEW("New", true);

    val route: String
        get() = this.toString()
}*/

@Composable
fun MainScreen(/*navController : NavHostController = rememberNavController()*/) {
    Column {
        Text(text = "Main Screen")
    }


}
