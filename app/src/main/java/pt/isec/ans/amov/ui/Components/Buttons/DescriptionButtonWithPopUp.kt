package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.Components.PopUps.PopUpBase
import pt.isec.ans.amov.ui.theme.BlueSoft

@Composable
fun DescriptionButtonWithPopUp(
    description: String,
    author: String,
) {
    var showPopup by remember { mutableStateOf(false) }  // This is usually inferred correctly as Boolean.

    // Placeholder for your sort button or trigger button
    Button(
        onClick = { showPopup = true },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .width(35.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.info),
            contentDescription = "image description",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(1.dp)
                .width(16.dp)
                .height(16.dp)
        )
    }


    if (showPopup) {
        PopUpBase(
            showDialog = showPopup,
            showActionButton = false,
            title = stringResource(R.string.description),
            buttonText = "",
            onDismiss = { showPopup = false },
            onConfirm = { showPopup = false },
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = description,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = BlueSoft,
                        )
                    )
                    Text(
                        text = "Created by: $author",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            color = BlueSoft,
                            textAlign = TextAlign.Right,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        )
    }
}



