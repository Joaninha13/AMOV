package pt.isec.ans.amov.ui.Screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft

data class CategoryFormState(
    var name: String = "",
    var description: String = "",
    var logo: String = "",
    var logoUri: Uri? = null
)

@Composable
fun AddCategory(viewModel: FireBaseViewModel) {
    var categoryFormState by remember { mutableStateOf(CategoryFormState()) }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //Log.d("VERRR------>", "result: $result") // passa aqui
                result.data?.data?.let { uri ->
                    // Aqui você tem a URI da imagem selecionada
                    // Agora você pode fazer o upload para o Firestore ou atualizar o estado conforme necessário
                    categoryFormState.logoUri = uri
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
                        text = "Contribute Category",
                        style = TextStyle(
                            fontSize = 24.sp,
                            //fontFamily = FontFamily(Font(R.font.inter)), esta linha da erro porque nao tem o ficheiro inter
                            fontWeight = FontWeight(600),
                            color = BlueHighlight,
                        )
                    )

                    Image(
                        modifier = Modifier
                            .padding(1.dp)
                            .width(16.dp)
                            .height(16.dp),
                        painter = painterResource(id = R.drawable.cancellationx1), //falta este resource
                        contentDescription = "Cancel",
                        contentScale = ContentScale.None
                    )

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

                    //First Input
                    TextInputs(categoryFormState){ updateState ->
                        categoryFormState = updateState
                    }

                    //Upload Logos
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
                                    append("Upload Logo")
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
                            text = "Upload Logo",
                            style = TextStyle(
                                fontSize = 16.sp,
                                //fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = BlueSoft,
                            )
                        )*/
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
                    categoryFormState.logoUri?.let { uri ->
                        // Quando o botão de registro é clicado, faz o upload da imagem
                        viewModel.uploadImage(uri) { imageUrl ->
                            // Após o upload bem-sucedido, atualiza o estado com a URL da imagem
                            categoryFormState = categoryFormState.copy(logo = imageUrl)

                            Log.d("VERRR------>", "CategoryFormState.name: ${categoryFormState.name}")
                            Log.d("VERRR------>", "CategoryFormState.description: ${categoryFormState.description}")
                            Log.d("VERRR------>", "CategoryFormState.logo: ${categoryFormState.logo}")
                            Log.d("VERRR------>", "CategoryFormState.logoUri: ${categoryFormState.logoUri}")

                            // Agora você pode salvar os dados do formulário no Firestore
                            viewModel.addCategory(
                                name = categoryFormState.name,
                                desc = categoryFormState.description,
                                image = categoryFormState.logo)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextInputs(categoryFormState: CategoryFormState, onCategoryFormState: (CategoryFormState) -> Unit) {

    //Name + Description
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
                _value = categoryFormState.name,
                _label = "Name",
                _iconName = R.drawable.nameicon,
                onValueChange = { newValue ->
                    categoryFormState.name = newValue
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
                _value = categoryFormState.description,
                _label = "Description",
                _iconName = R.drawable.descicon,
                onValueChange = { newValue ->
                    categoryFormState.description = newValue
                }
            )

        }
    }
}