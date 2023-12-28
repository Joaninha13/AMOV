package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.Components.PopUps.PopUpBase
import pt.isec.ans.amov.ui.theme.*
@Composable
fun SortButtonWithPopUp(
    sortOptions: List<String>,
    onSortSelected: (String) -> Unit // Callback when an option is selected
) {
    var showPopup by remember { mutableStateOf(false) }
    var selectedSortOption by remember { mutableStateOf("") }

    // When the sort option is selected from the popup, hide the popup and update the selected sort option
    val handleConfirm: () -> Unit = {
        showPopup = false
        onSortSelected(selectedSortOption)
    }

    // When the button is clicked, show the popup
    SortButton("Sort") {
        showPopup = true
    }

    // The PopUpBase is controlled by the showPopup state
    if (showPopup) {
        PopUpBase(
            showDialog = showPopup,
            showActionButton = true,
            title = "Sort Options",
            content = {
                // Content with list of sort options
                Column {
                    sortOptions.forEach { sortOption ->
                        val isSelected = sortOption == selectedSortOption
                        Text(
                            text = sortOption,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.inter_medium)),
                                color = if (isSelected) BlueHighlight else BlueSoft,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isSelected) BlueLighter else Color.Transparent, RoundedCornerShape(16.dp))
                                .clickable {
                                    selectedSortOption = sortOption // Update the selected sort option
                                }
                                .padding(16.dp)
                        )
                    }
                }
            },
            buttonText = "Done",
            onConfirm = handleConfirm,
            onDismiss = { showPopup = false }
        )
    }
}

@Preview
@Composable
fun SortButtonWithPopUpPreview() {
    val sortOptions = listOf("Option 1", "Option 2", "Option 3") // Dummy sort options
    var selectedSortCriteria by remember { mutableStateOf("") }

    SortButtonWithPopUp(sortOptions) { selectedOption ->
        // Update the selected sort criteria here
        selectedSortCriteria = selectedOption
        println("Selected Sort Criteria: $selectedSortCriteria")
    }
}


