package pt.isec.ans.amov.ui.Components.Cards

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.DangerRoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.Screen
import pt.isec.ans.amov.ui.ViewModels.FireBaseViewModel
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft
import pt.isec.ans.amov.ui.theme.Red

@Composable
fun UserCategoryCard(
    navController: NavController,
    viewModel: FireBaseViewModel,
    categoryName: String,
    numAttractions: Int,
    numApproved: Int,
    Logo: String,

    modifier: Modifier = Modifier,
){

    //Card Row
    Row(
        modifier = Modifier
            .width(330.dp)
            .height(100.dp)
            .padding(top = 5.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        //Left Column
        Column(
            modifier = Modifier
                .width(274.dp)
                .height(90.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {

            //Category Icon + Category Name
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                //Category Icon
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(14.dp)
                        .height(14.dp),
                    painter = rememberImagePainter(Logo),
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = categoryName,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = if (numApproved < 3) Red else BlueHighlight,
                    )
                )

            }

            //Number of attractions row
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.Start)
            ) {

                //Number of attractions
                Text(
                    text = "$numAttractions attractions",
                    style = TextStyle(
                        fontSize = 16.sp,
                        //fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(600),
                        color = BlueSoft,
                    )
                )
            }

            //Row with Button GO TO and Delete
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SecButton(_text = stringResource(R.string.go_to_page), onClick = {navController.navigate(Screen.InfoCategory.createRoute(categoryName))})
            }

        }

        //Right Column
        Column(
            modifier = Modifier
                .width(52.dp)
                .height(90.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
        ) {

            RoundIconButton(drawableId = R.drawable.edit, onClick = {navController.navigate(Screen.EditCategory.createRoute(categoryName))})
            DangerRoundIconButton(drawableId = R.drawable.trash, onClick = {
                if (numAttractions > 0) {
                    Toast.makeText(
                        navController.context,
                        navController.context.getString(R.string.can_t_delete_categories_with_attractions),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@DangerRoundIconButton
                }
                else{
                    viewModel.deleteCategory(categoryName)

                    Toast.makeText(navController.context, viewModel.error.value ?: navController.context.getString(R.string.category_deleted), Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

}

@Preview
@Composable
fun UserCategoryCardPreview(){
    /*UserCategoryCard(
        categoryName = "Monument",
        numAttractions = 4321
    )*/
}