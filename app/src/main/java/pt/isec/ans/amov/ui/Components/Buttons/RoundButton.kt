package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.ans.amov.R

@Composable
fun RoundIconButton(
    drawableId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(0.dp),
        shape = CircleShape,
        modifier = modifier
            .size(32.dp)
            .border(1.dp, Color.LightGray, CircleShape)
            .padding(2.dp)
            .aspectRatio(1f),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "Icon",
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
        )
    }
}

@Preview
@Composable
fun RoundIconButtonPreview() {
    RoundIconButton(drawableId = R.drawable.edit)
}
