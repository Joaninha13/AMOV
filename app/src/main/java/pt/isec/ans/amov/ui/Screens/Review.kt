package pt.isec.ans.amov.ui.Screens


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.ViewModels.LocationViewModel

data class ReviewFormState(
    var title: String = "",
    var description: String = "",
    var rating: Number = 0,
    var image: String = "",
    var imageUri: Uri? = null
)
@Composable
fun Review(navController: NavHostController, viewModelL: LocationViewModel, viewModelFB: FireBaseViewModel, attractionNames : String) {

    var reviewFormState by remember { mutableStateOf(ReviewFormState()) }

    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .width(360.dp)
                .height(600.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 16.dp))
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, top = 5.dp, end = 5.dp, bottom = 5.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(240.dp)
                        .height(100.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(240.dp)
                            .height(29.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(174.dp)
                                .height(29.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .width(174.dp)
                                    .height(29.dp),
                                text = "Writing Review",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.inter)),
                                    fontWeight = FontWeight(600),
                                    color = Color(0xCC0B394C)
                                )
                            )
                            // Child views Frame27.
                        }

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
                        // Child views Fram21.
                    }

                    // Child views Fram60.
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(240.dp)
                        .height(238.dp)
                ) {

                    ReviewInputs(reviewFormState = reviewFormState) {
                        reviewFormState = it
                    }

                    // Child views56.
                }

                GradientButton(
                    _text = "Submit",
                    _gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0B374B),
                            Color(0xFF00B6DE)
                        )
                    )
                ) {
                    reviewFormState.imageUri?.let { uri ->
                        viewModelFB.uploadImage(uri) { imageUrl ->
                            reviewFormState = reviewFormState.copy(image = imageUrl)

                            viewModelFB.addReview(
                                title = reviewFormState.title,
                                desc = reviewFormState.description,
                                rating = reviewFormState.rating,
                                image = reviewFormState.image,
                                attractionName = attractionNames
                            )
                        }
                    }

                    Toast.makeText(context, viewModelFB.error.value ?: "Add Succeed", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
                // Child views Frame3.
            }
            // Child views.
            //child Views do Inicio
        }
    }
}

@Composable
fun ReviewInputs(reviewFormState: ReviewFormState, onReviewFormState: (ReviewFormState) -> Unit){


    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Log.d("VERRR------>", "result: $result") // passa aqui
                result.data?.data?.let { uri ->
                    // Aqui você tem a URI da imagem selecionada
                    // Agora você pode fazer o upload para o Firestore ou atualizar o estado conforme necessário
                    reviewFormState.imageUri = uri
                }
            }
        }

    //Title
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp),
    ) {
        OutlinedInput(
            _value = reviewFormState.title,
            _label = "Title",
            _iconName = R.drawable.descicon,
            onValueChange = { newValue ->
                reviewFormState.title = newValue
            }
        )
    }

    //Description
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp),
    ) {
        OutlinedInput(
            _value = reviewFormState.description,
            _label = "Description",
            _iconName = R.drawable.descicon,
            onValueChange = { newValue ->
                reviewFormState.description = newValue
            }
        )
    }

    //Rating
    var rating by remember { mutableStateOf(0F) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(300.dp)
            .height(60.dp),
        ) {
            val formattedRating = "%.1f".format(rating)
            Text(text = "Rating: $formattedRating")

            Slider(
                value = rating,
                valueRange = 0F..3F,
                onValueChange = {
                    reviewFormState.rating = it
                    rating = it
                },
            )
        }

    //Image
    Column(
        modifier = Modifier
            .width(240.dp)
            .height(30.dp)
            .background(color = Color(0xCCFFFFFF), shape = RoundedCornerShape(size = 5.dp))
            .padding(start = 10.dp, end = 10.dp)
    ) {
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Upload one Image")
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