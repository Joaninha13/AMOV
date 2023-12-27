package pt.isec.ans.amov.ui.Components.PopUps

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.*

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.*

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.WarningsError


@Composable
fun DeletePopUp(
    showDialog: Boolean,
    showActionButton: Boolean = true,

    title: String,
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
                    textAlign = TextAlign.Start,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.inter_bold)),
                        color = WarningsError,
                    )
                )
            },

            confirmButton = {
                if (showActionButton) {
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.height(48.dp).width(121.dp),
                        colors = ButtonDefaults.buttonColors(WarningsError)
                    ) {
                        Text(
                            text = buttonText,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.inter_bold)),
                                color = BlueHighlight,
                            )
                        )
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.width(121.dp).height(48.dp),
                    colors = ButtonDefaults.buttonColors(BlueLighter)
                ) {
                    Text(
                        text = "Abort",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.inter_semibold)),
                            color = BlueHighlight,
                        )
                    )
                }
            },
            modifier = Modifier
                .width(320.dp)
                .padding(start = 10.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)
                .background(color = Color(0x1AFF0000), shape = RoundedCornerShape(size = 16.dp))
                .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
                .border(width = 6.dp, color = WarningsError, shape = RoundedCornerShape(size = 28.dp)))

    }
}


@Preview
@Composable
fun DeletePopUpPreview() {
    DeletePopUp(
        showDialog = true,
        showActionButton = true,

        title = "You are about to delete data",
        buttonText = "Delete",

        onConfirm = {
            Log.d("PopUpBase", "onConfirm")
        },
        onDismiss = {
            Log.d("PopUpBase", "onDismiss")
        }
    )
}