package pt.isec.ans.amov.ui.Components.Nav

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {
    private val _searchBarState = MutableStateFlow("")
    val searchBarState = _searchBarState.asStateFlow()

    fun setSearchBarState(state: String) {
        _searchBarState.value = state
    }
}