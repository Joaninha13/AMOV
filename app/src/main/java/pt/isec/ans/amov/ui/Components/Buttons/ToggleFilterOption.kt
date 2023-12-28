package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ToggleFilterOption(text: String, initialState: Boolean, filterFields: FilterFields) {

    var isToggled by remember { mutableStateOf(initialState) }

    ToggleSecButton(
        text = text,
        onToggleChanged = {
            isToggled = it
            filterFields.approved = isToggled // Directly modify the passed FilterFields
        },
        isToggled = isToggled
    )
}

