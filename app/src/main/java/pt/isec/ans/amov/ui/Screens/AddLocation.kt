package pt.isec.ans.amov.ui.Screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.GeoPoint
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter


data class LocationFormState(
    var country: String = "",
    var region: String = "",
    var description: String = "",
    var latitude : String = "",
    var longitude : String = "",
    var coordinates: GeoPoint = GeoPoint(0.0,0.0), //val coordinates = GeoPoint(latitude, longitude).
    var image: String = "",
    var imageUri: Uri? = null
)

@Composable
fun AddLocation(navController: NavHostController,viewModelL: LocationViewModel, viewModelFB: FireBaseViewModel) {

    var locationFormState by remember { mutableStateOf(LocationFormState()) }

    val context = LocalContext.current

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
                        text = "Contribute Location",
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
                        .height(600.dp)
                        .padding(start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(60.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {

                    //First inputs
                    TextInputs(locationFormState, viewModelL) { updatedState ->
                        locationFormState = updatedState
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
                    }

                }

                GradientButton(
                    _text = "Submit",
                    _gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0B374B),
                            Color(0xFF00B6DE)
                        )
                    )
                ){
                    try {
                        locationFormState.coordinates = GeoPoint(locationFormState.latitude.toDouble(), locationFormState.longitude.toDouble())
                    } catch (e: NumberFormatException) {
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


                    locationFormState.imageUri?.let { uri ->
                        // Quando o botão de registro é clicado, faz o upload da imagem
                        viewModelFB.uploadImage(uri) { imageUrl ->
                            // Após o upload bem-sucedido, atualiza o estado com a URL da imagem
                            locationFormState = locationFormState.copy(image = imageUrl)

                            // Agora você pode salvar os dados do formulário no Firestore
                            viewModelFB.addLocation(
                                locationFormState.country,
                                locationFormState.region,
                                locationFormState.description,
                                locationFormState.coordinates,
                                locationFormState.image)
                        }
                    }
                    Toast.makeText(context, viewModelFB.error.value ?: "Add Succeed", Toast.LENGTH_LONG).show()
                    navController.popBackStack()

                }
            }

        }

    }
}

@Composable
fun TextInputs(locationFormState: LocationFormState, viewModelL: LocationViewModel,onLocationFormState: (LocationFormState) -> Unit) {

    var enables by remember { mutableStateOf(false) }

    //Name + Description + Coordinates
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

        //latitude
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _width = 252.dp,
                _value = locationFormState.latitude,
                _label = if (!enables) "latitude" else locationFormState.latitude,
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    locationFormState.latitude = newValue
                }
            )
                //Icon container
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = BlueLighter,
                            shape = RoundedCornerShape(size = 50.dp)
                        )
                        .size(31.dp)
                        .padding(5.dp)
                        .clickable(onClick = {
                            viewModelL.currentLocation.observeForever { location ->
                                locationFormState.latitude = location.latitude.toString()
                                locationFormState.longitude = location.longitude.toString()
                            }
                            enables = !enables
                        }),
                    contentAlignment = Alignment.Center,
                ) {
                    // Icon
                    Image(
                        modifier = Modifier
                            .size(18.dp),
                        painter = painterResource(id = R.drawable.vector),
                        contentDescription = "Vector",
                        contentScale = ContentScale.None
                    )
                }

            }
        //longitude
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _width = 252.dp,
                _value = locationFormState.longitude,
                _label = if (!enables) "longitude" else locationFormState.longitude ,
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    locationFormState.longitude = newValue
                },
            )
        }
    }

}