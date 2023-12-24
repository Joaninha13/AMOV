package pt.isec.ans.amov.ui.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedInput(_width: Dp? = null, _value: String, _label: String, _iconName: Int, onValueChange: (String) -> Unit){
    var inputValue by remember { mutableStateOf(_value) }

    if(_width != null){
        OutlinedTextField(
            modifier = Modifier.width(_width),
            value = inputValue,
            label = { Text(text = _label) },
            leadingIcon = {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(18.dp)
                        .height(18.dp),
                    painter = painterResource(id = _iconName),
                    contentDescription = "name icon",
                    contentScale = ContentScale.None,
                )
            },
            onValueChange = {
                inputValue = it
                onValueChange(it)
            }
        )
    }else{
        OutlinedTextField(
            value = inputValue,
            label = { Text(text = _label) },
            leadingIcon = {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .width(18.dp)
                        .height(18.dp),
                    painter = painterResource(id = _iconName),
                    contentDescription = "name icon",
                    contentScale = ContentScale.None,
                )
            },
            onValueChange = {
                inputValue = it
                onValueChange(it)
            }
        )
    }

}