package pt.isec.ans.amov.ui.Components.Nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.BlueHighlight
import pt.isec.ans.amov.ui.theme.BlueLighter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavBarSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    trailingOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Boolean = false,

    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    searchViewModel: SearchViewModel
){
    val searchText = searchViewModel.searchBarState.collectAsState()



    //NavBar Container Row
    Row(
        modifier = Modifier
            .border(width = 2.dp, color = BlueLighter, shape = RoundedCornerShape(size = 50.dp))
            .height(50.dp)
            .width(230.dp)
            .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 50.dp)),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        SearchItem(
            onQueryChange = onQueryChange,
            trailingOnClick = trailingOnClick,
            trailingIcon = trailingIcon,
            coroutineScope = coroutineScope,
            modalBottomSheetState = modalBottomSheetState,
            searchViewModel= searchViewModel
        )

    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchItem(
    onQueryChange: (String) -> Unit,
    trailingOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Boolean = false,
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    searchViewModel: SearchViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchText = searchViewModel.searchBarState.collectAsState()  // Collecting as state

    Column {
        TextField(
            value = searchText.value,
            onValueChange = { newValue ->
                searchViewModel.setSearchBarState(newValue)
                onQueryChange(newValue)
            },
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
                        color = BlueLighter,
                    )
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    // Trigger the coroutine
                    onSearchTriggered(coroutineScope, modalBottomSheetState)

                },
            ),
            singleLine = true,
            modifier = modifier
                .padding(start = 5.dp, end = 5.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp)),
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
fun onSearchTriggered(coroutineScope: CoroutineScope, modalBottomSheetState: ModalBottomSheetState)
{
    coroutineScope.launch {
        if (modalBottomSheetState.isVisible) {
            modalBottomSheetState.hide()
        } else {
            modalBottomSheetState.show()
        }
    }
}


@Preview
@Composable
fun NavBarSearchPreview(){
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

//    NavBarSearch(
//        query = searchQuery,
//        onQueryChange = setSearchQuery,
//        trailingOnClick = { setSearchQuery("") },
//        modifier = Modifier,
//        trailingIcon = searchQuery.isNotEmpty(),
//
//    )

}
