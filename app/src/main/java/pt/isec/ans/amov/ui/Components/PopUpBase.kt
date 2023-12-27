package pt.isec.ans.amov.ui.Components

import android.util.Log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width

import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.*

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.WarningsError
import pt.isec.ans.amov.ui.theme.White40


@Composable
fun PopUpBase(
    showDialog: Boolean,
    title: String,
    content: @Composable () -> Unit,
    buttonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(pt.isec.ans.amov.R.font.inter_bold)),
                        color = BlueHighlight,
                    )
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
                ) {
                    content() // Dynamic content is inserted here
                }
            },


            confirmButton = {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.width(121.dp),
                    colors = ButtonDefaults.buttonColors(BlueHighlight)
                ) {
                    Text(
                        buttonText,
                        textAlign = TextAlign.Center // Center the button text
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.width(121.dp),
                    colors = ButtonDefaults.buttonColors(WarningsError)
                ) {
                    Text(
                        "Cancel",
                        textAlign = TextAlign.Center // Center the button text
                    )
                }
            },
            modifier = Modifier
                .width(300.dp)
                .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000)),

        containerColor = White40,


        )
    }
}


@Preview
@Composable
fun PopUpBasePreview() {
    PopUpBase(
        showDialog = true,
        title = "Title",
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, bottom = 26.dp)
            ) {

                SecButton("Button", onClick = {}, modifier = Modifier.fillMaxWidth())

                SecButton("Button 2", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        },
        buttonText = "Filter",
        onConfirm = {
            Log.d("PopUpBase", "onConfirm")
        },
        onDismiss = {
            Log.d("PopUpBase", "onDismiss")
        }
    )
}