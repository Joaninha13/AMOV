package pt.isec.ans.amov.ui.Screens

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.FilterField
import pt.isec.ans.amov.ui.Components.Buttons.FilterFields
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SearchDropdownButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import kotlin.math.log

@Composable
fun EditAttraction(
    navController: NavHostController,
    viewModelL: LocationViewModel,
    viewModelFB: FireBaseViewModel,
    nameToEdit : String,

){
    var newAttractionFormState by remember { mutableStateOf(AttractionFormState()) }

    var oldAttractionFormState by remember { mutableStateOf(AttractionFormState()) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //Page Column
        Column(
            modifier = Modifier
                .width(360.dp)
                .height(804.dp)
                .padding(start = 20.dp, top = 35.dp, end = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Form Column + Title + Submit Button
            Column(
                modifier = Modifier
                    .width(320.dp)
                    .height(676.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                //Title + Cancel button
                Row(
                    modifier = Modifier
                        .width(320.dp)
                        .height(29.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Text(
                        modifier = Modifier
                            .width(248.dp)
                            .height(29.dp),
                        text = "Edit Attraction",
                        style = TextStyle(
                            fontSize = 24.sp,
                            //fontFamily = FontFamily(Font(R.font.inter)), esta linha da erro porque nao tem o ficheiro inter
                            fontWeight = FontWeight(600),
                            color = BlueHighlight,
                        )
                    )

                    Box(
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(1.dp)
                                .width(16.dp)
                                .height(16.dp),
                            painter = painterResource(id = R.drawable.cancellationx1),
                            contentDescription = "Cancel",
                            contentScale = ContentScale.None
                        )
                    }

                }

                //Form Column
                Column(
                    modifier = Modifier
                        .width(320.dp)
                        .height(492.dp)
                        .padding(start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {

                    viewModelFB.getAttractions(nameToEdit){ desc ->
                        Log.d("VEERRRRR----->>>>>>", "desc: $desc")
                        oldAttractionFormState.name = desc[0]
                        oldAttractionFormState.description = desc[1]
                        oldAttractionFormState.category = desc[2]
                        oldAttractionFormState.location = desc[3]
                            oldAttractionFormState.image = listOf(desc[4])

                        Log.d("VEERRRRR----->>>>>>", "name: ${oldAttractionFormState.image}")


                        Log.d("VEERRRRR----->>>>>>", "category: ${oldAttractionFormState.category}")
                        Log.d("VEERRRRR----->>>>>>", "location: ${oldAttractionFormState.location}")


                        val geoPointValues = desc[5]
                            .replace("GeoPoint { latitude=", "")
                            .replace(" longitude=", "")
                            .replace("}", "")
                            .split(",")


                        if (geoPointValues.size == 2) {
                            oldAttractionFormState.latitude = geoPointValues[0].trim()
                            oldAttractionFormState.longitude = geoPointValues[1].trim()
                        }

                    }


                    //First 3 inputs
                    TextInputsEdit(newAttractionFormState,oldAttractionFormState) { updatedState ->
                        newAttractionFormState = updatedState
                    }

                    //Second 3 inputs
                    SecondInputsEdit(newAttractionFormState,oldAttractionFormState,viewModelFB) { updatedState ->
                        newAttractionFormState = updatedState
                    }

                }

                GradientButton(
                    _text = "Save",
                    _gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0B374B),
                            Color(0xFF00B6DE)
                        )
                    ),
                ){
                    try {
                        newAttractionFormState.coordinates = GeoPoint(newAttractionFormState.latitude.toDouble(), newAttractionFormState.longitude.toDouble())
                    }catch (e: NumberFormatException) {
                        // Se o usuário não inserir um número válido, aparece uma mensagem em cima a dizer coordenada inválidas
                        Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                        return@GradientButton
                    }catch (e: Exception) {
                        Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                        return@GradientButton
                    }catch (e: Error) {
                        Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                        return@GradientButton
                    }catch (e: Throwable) {
                        Toast.makeText(context, "Invalid coordinates", Toast.LENGTH_SHORT).show()
                        return@GradientButton
                    }
                    newAttractionFormState.imageUri.let { uri ->
                        // Quando o botão de registro é clicado, faz o upload da imagem
                        viewModelFB.uploadImages(uri) { imageUrl ->
                            newAttractionFormState = newAttractionFormState.copy(image = imageUrl)
                        }
                    }

                    viewModelFB.updateAttractions(
                        attractionName = nameToEdit,
                        name = newAttractionFormState.name,
                        desc = newAttractionFormState.description,
                        coordinates = newAttractionFormState.coordinates,
                        category = newAttractionFormState.category,
                        Location = newAttractionFormState.location,
                        images = newAttractionFormState.image
                    )

                    Toast.makeText(context, viewModelFB.error.value ?: "Update Succeed", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }

            }
        }
    }
}

@Composable
fun TextInputsEdit(newAttractionFormState: AttractionFormState, oldAttractionFormState: AttractionFormState, onAttractionFormStateChange: (AttractionFormState) -> Unit) {


    newAttractionFormState.name = oldAttractionFormState.name
    newAttractionFormState.description = oldAttractionFormState.description
    newAttractionFormState.latitude = oldAttractionFormState.latitude
    newAttractionFormState.longitude = oldAttractionFormState.longitude
    newAttractionFormState.image = oldAttractionFormState.image

    //Name + Description + Coordinates
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
        ){


            OutlinedInput(
                _value = oldAttractionFormState.name,
                _label = oldAttractionFormState.name,
                _iconName = R.drawable.nameicon,
                onValueChange = { newValue ->
                    newAttractionFormState.name = newValue
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
        ){

            OutlinedInput(
                _value = oldAttractionFormState.description,
                _label = oldAttractionFormState.description,
                _iconName = R.drawable.descicon,
                onValueChange = { newValue ->
                    newAttractionFormState.description = newValue
                }
            )
        }

        //latitude
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){

            OutlinedInput(
                _width = 252.dp,
                _value = oldAttractionFormState.latitude,
                _label = oldAttractionFormState.latitude,
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    newAttractionFormState.latitude = newValue
                }
            )

            //Icon container
            RoundIconButton(
                drawableId = R.drawable.vector,
                modifier = Modifier
                    .size(40.dp)
            )

        }
        //longitude
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _width = 252.dp,
                _value = oldAttractionFormState.longitude,
                _label = oldAttractionFormState.longitude,
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    newAttractionFormState.longitude = newValue
                }
            )
        }
    }

}

@Composable
fun SecondInputsEdit(newAttractionFormState: AttractionFormState, oldAttractionFormState: AttractionFormState, viewModelFB: FireBaseViewModel, onAttractionFormStateChange: (AttractionFormState) -> Unit){

    var inputValues by remember { mutableStateOf(FilterFields()) }

    var categoriesList by remember { mutableStateOf<List<String>>(emptyList()) }

    var locationList by remember { mutableStateOf<List<String>>(emptyList()) }

    viewModelFB.getAllCategories { loadedCategories ->
        categoriesList = loadedCategories
    }

    viewModelFB.getAllLocations { loadedLocations ->
        locationList = loadedLocations
    }

    newAttractionFormState.category = oldAttractionFormState.category
    newAttractionFormState.location = oldAttractionFormState.location


    val pickImagesLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { results ->
            val uris = results.mapNotNull { it }
            newAttractionFormState.imageUri = uris

        }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {

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

            SearchDropdownButton(FilterField.CATEGORY, oldAttractionFormState.category, inputValues, categoriesList){ newValue ->
                newAttractionFormState.category = newValue
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

            SearchDropdownButton(FilterField.LOCATION, oldAttractionFormState.location, inputValues, locationList){ newValue ->
                newAttractionFormState.location = newValue
            }
        }

        //Upload Images
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

}
