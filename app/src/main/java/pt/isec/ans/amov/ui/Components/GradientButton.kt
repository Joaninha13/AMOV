package pt.isec.ans.amov.ui.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GradientButton(
    _text: String,
    _gradient: Brush,
    onClick: () -> Unit = { },)
{

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .background(_gradient, shape = CircleShape),
            /*.background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF0B374B),
                        Color(0xFF00B6DE)
                    )
                ),
                shape = CircleShape
            ),*/
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.padding(5.dp),
            text = _text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }

}