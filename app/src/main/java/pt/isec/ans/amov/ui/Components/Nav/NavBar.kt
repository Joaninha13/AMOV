package pt.isec.ans.amov.ui.Components.Nav

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton
import pt.isec.ans.amov.ui.Screen

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavBar(
    navController: NavController,
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState,
    searchViewModel: SearchViewModel

    ){
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        RoundIconButton(
            drawableId = R.drawable.eiffel_tower,
            onClick = {
                onSearchTriggered(coroutineScope, modalBottomSheetState)
                searchViewModel.onSearchTextChanged("Attractions")
            },
            modifier = Modifier
                .size(50.dp)
                .border(2.dp, Color.LightGray, CircleShape)
                .background(color = Color(0xFFFFFFFF), shape = CircleShape)
        )

        NavBarSearch(
            query = searchQuery,
            onQueryChange = setSearchQuery,
            trailingOnClick = { setSearchQuery("") },
            trailingIcon = searchQuery.isNotEmpty(),
            coroutineScope = coroutineScope,
            modalBottomSheetState = modalBottomSheetState,
            searchViewModel= searchViewModel
        )

        RoundIconButton(
            drawableId = R.drawable.account,
            onClick = {
                navController.navigate(Screen.ViewAccount.route)
            },
            modifier = Modifier
                .size(50.dp)
                .border(2.dp, Color.LightGray, CircleShape)
                .background(color = Color(0xFFFFFFFF), shape = CircleShape)

        )
    }

}

