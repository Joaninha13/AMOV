package pt.isec.ans.amov.ui.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import pt.isec.ans.amov.R

@Composable
fun NavBar(){
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .width(340.dp)
            .height(50.dp)
            .zIndex(1f),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        RoundIconButton(drawableId = R.drawable.monument)

        NavBarSearch(
            query = searchQuery,
            onQueryChange = setSearchQuery,
            trailingOnClick = { setSearchQuery("") },
            modifier = Modifier,
            trailingIcon = searchQuery.isNotEmpty()
        )

        RoundIconButton(drawableId = R.drawable.account)
    }

}