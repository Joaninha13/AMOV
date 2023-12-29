package pt.isec.ans.amov.ui.Components.Buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.Components.OutlinedInput
import pt.isec.ans.amov.ui.Components.PopUps.PopUpBase

@Composable
fun FilterButtonWithPopUp(
    filterOptions: List<@Composable () -> Unit>,
    filterData: FilterFields,
    onFilterApplied: (FilterFields) -> Unit
) {
    var showPopup by remember { mutableStateOf(false) }  // This is usually inferred correctly as Boolean.

    // Placeholder for your sort button or trigger button
    Button(
        onClick = { showPopup = true },
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        modifier = Modifier
            .width(35.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.filters),
            contentDescription = "image description",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(1.dp)
                .width(24.dp)
                .height(24.dp)
        )
    }


    if (showPopup) {
        PopUpBase(
            showDialog = showPopup,
            title = "Filter Options",
            buttonText = "Done",
            onDismiss = { showPopup = false },
            onConfirm = {
                showPopup = false
                onFilterApplied(filterData) // Pass the potentially updated FilterFields back
            },
            content = {
                Column(
                        verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.Top),
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(top = 15.dp, bottom = 15.dp)
                    ) {
                    filterOptions.forEach { filterOption ->
                        filterOption() // Invoking each composable lambda
                    }
                }
            }
        )
    }
}



