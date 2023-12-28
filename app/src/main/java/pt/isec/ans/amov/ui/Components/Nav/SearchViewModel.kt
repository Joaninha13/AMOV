package pt.isec.ans.amov.ui.Components.Nav

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    val searchText = mutableStateOf("")

    fun onSearchTextChanged(newText: String) {
        searchText.value = newText
    }
}