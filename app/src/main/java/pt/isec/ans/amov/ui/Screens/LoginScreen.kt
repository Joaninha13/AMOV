package pt.isec.ans.amov.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel

@Composable
fun LoginScreen(navController: NavHostController, viewModel: FireBaseViewModel, onSuccess : () -> Unit) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val error by remember { viewModel.error }
    val user by remember { viewModel.user } // se quiser mostrar algo sobre o user

    LaunchedEffect(key1 = user){
        if (user != null && error == null)
            onSuccess()
    }

    Column(//frame home
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        if (error != null) {
            Text("Error: $error", Modifier.background(Color(255, 0, 0)))
            Spacer(modifier = Modifier.height(16.dp))
        }
        Column(//frame3
            verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .width(360.dp)
                .height(604.dp)
                .padding(start = 60.dp, top = 30.dp, end = 60.dp, bottom = 30.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(240.dp)
                    .height(168.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login_logo),
                    contentDescription = "APP",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                )
                Row( //frame 6
                    horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.Start),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .width(240.dp)
                        .height(48.dp)
                ) {
                    Column(//frame Title
                        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .width(240.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .width(240.dp)
                                .height(48.dp),
                            text = "Turisroute",
                            style = TextStyle(
                                fontSize = 40.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(800),
                                color = Color(0xFF0C394C),
                                textAlign = TextAlign.Center,
                            )
                        )
                        // Child views title.
                    }
                    // Child views 6.
                }
                // Child views 7.
            } // frame 7

            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(240.dp)
                    .height(218.dp)
            ) {
                Column(
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
            ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(1.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp)
                        ) {
                            Image(
                                modifier = Modifier
                                    .padding(1.dp)
                                    .height(50.dp)
                                    .width(20.dp),
                                painter = painterResource(id = R.drawable.email),
                                contentDescription = "image description",
                                contentScale = ContentScale.None
                            )
                            //substituir texto por outlines
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(55.dp),
                                value = email.value,
                                onValueChange = { email.value = it },
                                label = { Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    text = "Email",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily(Font(R.font.inter)),
                                        fontWeight = FontWeight(400),
                                        color =  Color(0xFF9C9C9C),
                                    )
                                ) }
                            )
                            // Child views row.
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(1.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(1.dp)
                                .height(50.dp)
                                .width(20.dp),
                            painter = painterResource(id = R.drawable.pass),
                            contentDescription = "image description",
                            contentScale = ContentScale.None
                        )
                        //substituir texto por outlines

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp),
                            value = password.value,
                            onValueChange = { password.value = it },
                            label = { Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                text = "Password",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.inter)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF9C9C9C),
                                )
                            ) }
                        )
                        // Child views row.
                    }
                    // Child views input1.
                }
                // Child views 4.
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Button(
                    onClick = {viewModel.signInWithEmail(email.value, password.value)},
                    modifier = Modifier
                        .width(120.dp)) {
                        Text(text = "Login")
                    }
                    // Child views row.
                }
                // Child views 5.
            }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .width(240.dp)
                .height(58.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                Text(
                    text = "Don’t have an account? ",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xD9B6B5B5),
                        textAlign = TextAlign.Center,
                    )
                )
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append("Sign up")
                        }
                    },
                    onClick = { offset ->
                        // Handle click on "Sign up"
                        // Navigate to the signup screen or perform any other action
                    },
                    modifier = Modifier.clickable {
                        // Optional: You can specify additional click behavior here
                        // For example, to navigate to another screen
                    }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Project Credits - ",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xD9B6B5B5),
                        textAlign = TextAlign.Center,
                    )
                )
                ClickableText(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue)) {
                            append("Take a look")
                        }
                    },
                    onClick = { offset ->
                        // Handle click on "Sign up"
                        // Navigate to the signup screen or perform any other action
                    },
                    modifier = Modifier.clickable {
                        // Optional: You can specify additional click behavior here
                        // For example, to navigate to another screen
                    }
                )
            }
            // Child views.
        }

            // Child views 3.
        }
    }

@Preview
@Composable
fun MainScreenPreview() {
    //MainScreen()
}
