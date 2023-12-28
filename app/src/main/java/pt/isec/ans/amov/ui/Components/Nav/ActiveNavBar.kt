package pt.isec.ans.amov.ui.Components.Nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import kotlinx.coroutines.CoroutineScope
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.Buttons.RoundIconButton

@Preview
@Composable
fun ActiveNavBar(
    ){
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(10.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
    ) {


        ActiveNavBarSearch(
            query = searchQuery,
            onQueryChange = setSearchQuery,
            trailingOnClick = { setSearchQuery("") },
        )
    }

}