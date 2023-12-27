package pt.isec.ans.amov.ui.Components.PopUps

import android.util.Log
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.width

import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.*

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.Components.Buttons.SecButton
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueSoft
import pt.isec.ans.amov.ui.theme.White40
import pt.isec.ans.amov.ui.theme.White80


@Composable
fun PopUpBase(
    showDialog: Boolean,
    showActionButton: Boolean = true,

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.inter_bold)),
                            color = BlueHighlight,
                        )
                    )
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
                if (showActionButton) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally // Center the content horizontally
                    ) {
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.height(48.dp),
                            colors = ButtonDefaults.buttonColors(BlueHighlight)
                        ) {
                            Text(
                                text = buttonText,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.inter_semibold)),
                                    color = White80,
                                )
                            )
                        }
                    }
                }
            },

            modifier = Modifier
                .width(300.dp)
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                ),

        containerColor = White40,


        )
    }
}


@Preview
@Composable
fun PopUpBasePreview1() {
    var description: String = ""

    PopUpBase(
        showDialog = true,
        showActionButton = true,
        title = "Title",
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp, bottom = 26.dp)
            ) {

                SecButton("Button", onClick = {}, modifier = Modifier.fillMaxWidth())

                SecButton("Button 2", onClick = {}, modifier = Modifier.fillMaxWidth().height(58.dp))

                OutlinedInput(
                    _value = description,
                    _label = "Description",
                    _iconName = R.drawable.descicon,
                    onValueChange = { newValue ->
                        description = newValue
                    }
                )
            }
        },
        buttonText = "Submit",
        onConfirm = {
            Log.d("PopUpBase", "onConfirm")
        },
        onDismiss = {
            Log.d("PopUpBase", "onDismiss")
        }
    )
}

@Preview
@Composable
fun PopUpBasePreview2() {
    PopUpBase(
        showDialog = true,
        showActionButton = false,
        title = "Description",
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {

                Text(
                    text = "This category features enduring landmarks of historical and cultural importance. These structures commemorate notable figures and events, offering visitors a glimpse into the area's heritage.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        fontWeight = FontWeight(600),
                        color = BlueSoft,
                    )
                )

                Text(
                    text = "From Jorge",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.inter_semibold)),
                        fontWeight = FontWeight(600),
                        color = BlueSoft,
                        textAlign = TextAlign.Right,
                    )
                )
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