package pt.isec.ans.amov.ui.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pt.isec.ans.amov.R
import pt.isec.ans.amov.dataStructures.AttractionDetails
import pt.isec.ans.amov.dataStructures.ReviewsDetails
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundImageUser
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.Components.Buttons.SortButtonWithPopUp
import pt.isec.ans.amov.ui.Components.Cards.UserReviewCard
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft


@Composable
fun ViewAccount(
    navController: NavController,
    viewModel: FireBaseViewModel
) {
    val sortOptions = listOf("Option 1", "Option 2", "Option 3") // Dummy sort options
    var selectedSortCriteria by remember { mutableStateOf("") }

    var reviewList by remember { mutableStateOf<List<ReviewsDetails>>(emptyList()) }
    val context = LocalContext.current

    var UserName by remember { mutableStateOf("") }
    var Photo by remember { mutableStateOf("") }
    var numCat by remember { mutableStateOf(0) }
    var numLoc by remember { mutableStateOf(0) }
    var numAtt by remember { mutableStateOf(0) }

    viewModel.getNumCategoriesByUser { num ->
        numCat = num
    }
    viewModel.getNumLocationsByUser { num ->
        numLoc = num
    }
    viewModel.getNumAttractionsByUser { num ->
        numAtt = num
    }

    viewModel.getUserDetails{ name, photo ->
         UserName = name
         Photo = photo
    }

    Log.d("ViewAccount", "ViewAccount: $UserName")
    Log.d("ViewAccount", "ViewAccount: $Photo")

    // Buscar categorias quando o Composable for iniciado
    DisposableEffect(viewModel) {
        viewModel.getAllReviewsByUser { review, error ->
            if (error == null) {
                reviewList = review
            } else {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        // Cancelar o efeito quando o Composable for removido
        onDispose { }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        //Page Column
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 20.dp, top = 35.dp, end = 20.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ){
                //Title + Button row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    //Title
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RoundImageUser(
                            drawable = Photo,
                            /*onClick = {
                                viewModel.signOut()
                                navController.navigate(Screen.LoginScreen.route)
                            },*/
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = UserName,
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                                    color = BlueHighlight,
                                )
                            )
                        }
                    }

                    //Close Button
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    GradientButton(
                        _text = "Log Out",
                        _gradient = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0B374B),
                                Color(0xFF00B6DE)
                            )
                        )
                    ){
                        viewModel.signOut()
                        navController.navigate(Screen.LoginScreen.route)
                    }
                }
            }


            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Contributions",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        color = BlueHighlight,
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ViewAttraction.route)
                            }
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Attractions",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                                        color = BlueSoft,
                                    )
                                )
                                Text(
                                    text = numAtt.toString(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                                        color = BlueHighlight,
                                    )
                                )
                            }
                        }

                        SecButton(_text = "Add More", onClick = {
                            navController.navigate(Screen.AddAttractions.route)
                        })
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ViewLocation.route)
                            }
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Locations",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                                        color = BlueSoft,
                                    )
                                )
                                Text(
                                    text = numLoc.toString(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                                        color = BlueHighlight,
                                    )
                                )
                            }
                        }

                        SecButton(_text = "Add More", onClick = {
                            navController.navigate(Screen.AddLocations.route)
                        })
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.ViewCategory.route)
                            }
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = "Categories",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.inter_medium)),
                                        color = BlueSoft,
                                    )
                                )
                                Text(
                                    text = numCat.toString(),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center,
                                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                                        color = BlueHighlight,
                                    )
                                )
                            }
                        }

                        SecButton(_text = "Add More", onClick = {
                            navController.navigate(Screen.AddCategories.route)
                        })
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Reviews",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily(Font(R.font.inter_semibold)),
                                color = BlueHighlight,
                            )
                        )
                        Text(
                            text = reviewList.size.toString(),
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter_semibold)),
                                color = BlueHighlight,
                            )
                        )
                    }
                    SortButtonWithPopUp(sortOptions) { selectedOption ->
                        // Update the selected sort criteria here
                        selectedSortCriteria = selectedOption
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(reviewList.size) { index ->
                            UserReviewCard(
                                navController = navController,
                                viewModel = viewModel,
                                reviewId = reviewList[index].reviewId,
                                attraction = reviewList[index].attractionId,
                                title = reviewList[index].title,
                                comment = reviewList[index].comment,
                                rating = reviewList[index].rating,
                            )
                        }
                    }
                }


            }


        }
    }
}

@Preview
@Composable
fun ViewAccountPreview() {
    //ViewAccount(navController = NavController(LocalContext.current))
}