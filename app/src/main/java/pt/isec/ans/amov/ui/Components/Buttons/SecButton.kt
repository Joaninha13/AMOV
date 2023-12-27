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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.*

@Composable
fun SecButton(
    _text: String,
    onClick: () -> Unit = { },
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(0.dp),
        modifier = modifier
            .border(width = 1.dp, color = BlueLighter, shape = RoundedCornerShape(5.dp))
            .background(
                color = Color(0xCCFFFFFF),
                shape = RoundedCornerShape(5.dp)
            )
            .wrapContentSize(align = Alignment.Center)
            .requiredSizeIn(minWidth = 70.dp)
            .height(30.dp)
            .padding(start = 10.dp, end = 10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        )
    ) {
        Text(
            text = _text,
            fontFamily = FontFamily(Font(R.font.inter_medium)),
            fontSize = 14.sp,
            color = BlueHighlight,
            modifier = Modifier.padding(end = 5.dp)
        )
    }
}



@Preview
@Composable
fun SecButtonPreview() {
    SecButton("Sort")
}
