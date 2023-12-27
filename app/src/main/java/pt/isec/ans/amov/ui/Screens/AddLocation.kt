package pt.isec.ans.amov.ui.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.GradientButton
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft

data class LocationFormState(
    var country: String = "",
    var region: String = "",
    var description: String = "",
    var coordinates: String = ""
)

@Preview
@Composable
fun AddLocation(){

    var locationFormState by remember { mutableStateOf(LocationFormState()) }

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

                    //First inputs
                    TextInputs(locationFormState) { updatedState ->
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

                        Text(
                            text = "Upload Images",
                            style = TextStyle(
                                fontSize = 16.sp,
                                //fontFamily = FontFamily(Font(R.font.inter)),
                                fontWeight = FontWeight(500),
                                color = BlueSoft,
                            )
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
                ){ //TODO implement lambda when things work on firebase
                    Log.d("D", "submit")
                }
            }

        }

    }
}

@Composable
fun TextInputs(locationFormState: LocationFormState, onLocationFormState: (LocationFormState) -> Unit) {
    var country by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var coordinates by remember { mutableStateOf("") }

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
        ){

            OutlinedInput(
                _value = country,
                _label = "Country",
                _iconName = R.drawable.nameicon,
                onValueChange = { newValue ->
                    country = newValue
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
        ){

            OutlinedInput(
                _value = region,
                _label = "Region",
                _iconName = R.drawable.nameicon,
                onValueChange = { newValue ->
                    region = newValue
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
                _value = description,
                _label = "Description",
                _iconName = R.drawable.descicon,
                onValueChange = { newValue ->
                    description = newValue
                }
            )

        }

        //Coordinates
        Row(
            modifier = Modifier
                .width(300.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){

            OutlinedInput(
                _width = 252.dp,
                _value = coordinates,
                _label = "Coordinates",
                _iconName = R.drawable.coordsicon,
                onValueChange = { newValue ->
                    coordinates = newValue
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
                    .padding(5.dp),
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
    }

}