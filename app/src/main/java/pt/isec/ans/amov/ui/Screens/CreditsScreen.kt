package pt.isec.ans.amov.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter

@Composable
fun CreditsScreen(
    navController: NavHostController
){

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //Page Column
        Column(
            modifier = Modifier
                .width(360.dp)
                .height(804.dp)
                .padding(top = 35.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Credits
            Column(
                modifier = Modifier
                    .width(360.dp)
                    .fillMaxHeight()
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(70.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                //Title + Cancel button + Texts
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .height(128.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    //Title + Cancel button
                    Row(
                        Modifier
                            .width(300.dp)
                            .height(76.dp)
                            .padding(start = 8.dp, end = 8.dp, bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Text(
                            modifier = Modifier
                                .width(239.dp)
                                .height(36.dp),
                            text = "Credits",
                            style = TextStyle(
                                fontSize = 35.sp,
                                fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(800),
                                color = Color(0xFF000000),
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

                    Text(
                        modifier = Modifier
                            .width(262.dp)
                            .height(16.dp),
                        text = "ISEC - 2023/2024 - Mobile Architectures",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFB6B5B5),
                        )
                    )

                    Text(
                        modifier = Modifier
                            .width(297.dp)
                            .height(16.dp),
                        text = "Software Engineering / Engenharia Informática",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFB6B5B5),
                        )
                    )

                }

                //Personal Cards Column
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Row 1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Personal Card
                        PersonalCard("Bruno Gomes", R.drawable.bruno)
                        // Space between cards
                        Spacer(modifier = Modifier.width(25.dp))
                        // Personal Card
                        PersonalCard("João Carvalho", R.drawable.joao)
                    }

                    // Row 2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Personal Card
                        PersonalCard("Jorge Soares", R.drawable.jorge)
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalCard(name: String, imageRes: Int) {

    Column(
        modifier = Modifier
            .width(165.dp)
            .height(200.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = name,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(800),
                color = Color(0xFF000000),
            ),
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .border(width = 0.dp, color = BlueLighter, shape = RoundedCornerShape(10.dp))
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "image description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp))

            )
        }
    }
}