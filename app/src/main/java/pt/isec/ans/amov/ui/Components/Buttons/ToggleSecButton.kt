package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.*

@Composable
fun ToggleSecButton(
    text: String,
    onToggleChanged: (Boolean) -> Unit, // Callback with the new state
    isToggled: Boolean, // Current state of the toggle
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isToggled) BlueHighlight else Color(0xCCFFFFFF) // Change colors based on the state
    val textColor = if (isToggled) Color.White else BlueHighlight

    Button(
        onClick = { onToggleChanged(!isToggled) },
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(0.dp),
        modifier = modifier
            .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(5.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(5.dp))
            .wrapContentSize(align = Alignment.Center)
            .requiredSizeIn(minWidth = 70.dp)
            .height(50.dp)
            .padding(start = 10.dp, end = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        )
    ) {
        Text(
            text = text,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier.padding(end = 5.dp).fillMaxWidth()
        )
    }
}

@Composable
@Preview
fun ToggleSecButtonPreview() {
    var isToggled by remember { mutableStateOf(false) } // Remember the toggle state

    ToggleSecButton(
        text = if (isToggled) "On" else "Off", // Change text based on state
        onToggleChanged = { isToggled = it }, // Update the state on toggle
        isToggled = isToggled, // Current state
        modifier = Modifier.padding(8.dp)
    )
}
