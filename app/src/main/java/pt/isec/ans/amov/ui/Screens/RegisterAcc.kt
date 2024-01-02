package pt.isec.ans.amov.ui.Screens


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft

data class RegisterAccFormState(
    var name: String = "",
    var Email: String = "",
    var Password: String = "",
    var ConfirmPassword: String = "",
    var profileImageUrl: String = ""
)

@Composable
fun RegisterAcc(navController: NavHostController, viewModel: FireBaseViewModel) {
    var registerAccFormState by remember { mutableStateOf(RegisterAccFormState()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .width(360.dp)
                .height(1000.dp)
                .padding(start = 60.dp, top = 60.dp, end = 60.dp, bottom = 60.dp)
        ) {

            Row(
                modifier = Modifier
                    .width(320.dp)
                    .height(46.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ){
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .width(200.dp)
                        .height(46.dp)
                ) {

                    Text(
                        modifier = Modifier
                            .width(80.dp)
                            .height(25.dp),
                        text = "Sign Up",
                        style = TextStyle(
                            fontSize = 21.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(800),
                            color = Color(0xFF000000),
                        )
                    )
                    Text(
                        modifier = Modifier
                            .width(179.dp)
                            .height(16.dp),
                        text = "Create a brand new account",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFB6B5B5),
                        )
                    )
                    // Child views Title.
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
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(325.dp)
                    //.fillMaxSize(),
            ) {

                TextInputs(registerAccFormState, { registerAccFormState = it }, { uri ->
                    selectedImageUri = uri
                })

            }

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center,
            ) {
                GradientButton(
                    _text = "Submit",
                    _gradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0B374B),
                            Color(0xFF00B6DE)
                        )
                    )
                ) {
                    selectedImageUri?.let { uri ->
                        viewModel.uploadImage(uri) { imageUrl ->
                            registerAccFormState = registerAccFormState.copy(profileImageUrl = imageUrl)

                            if (registerAccFormState.Password == registerAccFormState.ConfirmPassword) {
                                viewModel.createUserWithEmail(
                                    registerAccFormState.Email,
                                    registerAccFormState.Password,
                                    registerAccFormState.name,
                                    registerAccFormState.profileImageUrl
                                )
                            } else {
                                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_LONG).show()
                                return@uploadImage
                            }
                            Toast.makeText(context, viewModel.error.value ?: "Add Succeed", Toast.LENGTH_LONG).show()

                            /*viewModel.addUser(
                                registerAccFormState.name,
                                registerAccFormState.profileImageUrl
                            )*/

                            navController.navigate(Screen.LoginScreen.route)
                        }
                    }
                }
            }

        }

    }
}

@Composable
fun TextInputs(registerAccFormState: RegisterAccFormState, onRegisterAccFormState: (RegisterAccFormState) -> Unit, onImageSelected: (Uri) -> Unit) {

    //Name + Description
    Column(
        modifier = Modifier
            .fillMaxSize(),
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
        ) {

            OutlinedInput(
                _value = registerAccFormState.name,
                _label = "Name",
                _iconName = R.drawable.nameicon,
                onValueChange = { newValue ->
                    registerAccFormState.name = newValue
                }
            )
        }

        //Email
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _value = registerAccFormState.Email,
                _label = "Email",
                _iconName = R.drawable.email,
                onValueChange = { newValue ->
                    registerAccFormState.Email = newValue
                }
            )
        }

        //Pass
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _value = registerAccFormState.Password,
                _label = "Password",
                _iconName = R.drawable.pass,
                onValueChange = { newValue ->
                    registerAccFormState.Password = newValue
                }
            )
        }

        //ConfirmePass
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            OutlinedInput(
                _value = registerAccFormState.ConfirmPassword,
                _label = "ConfirmPassword",
                _iconName = R.drawable.pass,
                onValueChange = { newValue ->
                    registerAccFormState.ConfirmPassword = newValue
                }
            )
        }

        // Image
        val pickImageLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        // Aqui você tem a URI da imagem selecionada
                        // Agora você pode fazer o upload para o Firestore ou atualizar o estado conforme necessário
                        onImageSelected(uri)
                    }
                }
            }

        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append("Upload Profile Image")
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
}

@Preview
@Composable
fun RegisterAccPreview() {
    //RegisterAcc()
}