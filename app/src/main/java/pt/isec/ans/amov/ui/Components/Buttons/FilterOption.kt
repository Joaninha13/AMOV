package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.runtime.Composable

interface FilterOption {
    val identifier: String  // Unique identifier for the filter option
    @Composable
    fun Content(onValueChanged: (String) -> Unit)
}
