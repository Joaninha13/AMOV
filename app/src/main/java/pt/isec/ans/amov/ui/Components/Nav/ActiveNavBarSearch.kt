package pt.isec.ans.amov.ui.Components.Nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter
import pt.isec.ans.amov.ui.theme.BlueSoft

@Composable
fun ActiveNavBarSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    trailingOnClick: () -> Unit,
    modifier: Modifier = Modifier,
){

    //NavBar Container Row
    Row(
        modifier = Modifier
            .border(width = 2.dp, color = BlueLighter, shape = RoundedCornerShape(size = 50.dp))
            .width(250.dp)
            .height(50.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 50.dp)),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        ActiveSearchItem(
            query = query,
            onQueryChange = onQueryChange,
            trailingOnClick = trailingOnClick,
        )

    }

}

@Composable
fun ActiveSearchItem(
    query: String,
    onQueryChange: (String) -> Unit,
    trailingOnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        colors = TextFieldDefaults.colors(
            focusedTextColor = BlueHighlight,
            unfocusedTextColor = BlueHighlight,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                stringResource(R.string.search),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.inter_bold)),
                    color = BlueHighlight,
                )
            )
        },
        trailingIcon = {
            IconButton(onClick = trailingOnClick) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "image description",
                    tint = BlueSoft
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        ),
        singleLine = true,
        modifier = modifier
            .padding(start = 5.dp, end = 5.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp)),
    )
}

@Preview
@Composable
fun ActiveNavBarSearchPreview(){
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    ActiveNavBarSearch(
        query = searchQuery,
        onQueryChange = setSearchQuery,
        trailingOnClick = { setSearchQuery("") },
        modifier = Modifier,
    )

}