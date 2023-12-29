package pt.isec.ans.amov.ui.Components.Buttons

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import com.google.android.gms.tasks.OnSuccessListener
import pt.isec.ans.amov.R
import pt.isec.ans.amov.ui.theme.*

@Composable
fun SearchDropdownButton(
    field: FilterField, // Enum to specify which field to update
    text: String,
    filterFields: FilterFields,
    items: List<String>,
    onSuccessListener: OnSuccessListener<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember {
        mutableStateOf(
            when (field) {
                FilterField.CATEGORY -> filterFields.category
                FilterField.LOCATION -> filterFields.location
                FilterField.COUNTRY -> filterFields.country
            }
        )
    }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column {
        TextField(
            value = selectedText,
            onValueChange = {
                selectedText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = { Text(text) },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            items.filter { it.contains(selectedText, ignoreCase = true) }.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        selectedText = label
                        when (field) {
                            FilterField.CATEGORY -> filterFields.category = label
                            FilterField.LOCATION -> filterFields.location = label
                            FilterField.COUNTRY -> filterFields.country = label
                        }
                        onSuccessListener.onSuccess(label)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = label,
                            fontFamily = FontFamily(Font(R.font.inter_medium)),
                            fontSize = 14.sp,
                            color = BlueHighlight,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                )
            }
        }
    }
}

