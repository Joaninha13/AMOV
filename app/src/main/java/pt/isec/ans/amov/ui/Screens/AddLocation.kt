package pt.isec.ans.amov.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight

@Composable
fun AddLocation(){

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

            }

        }

    }
}