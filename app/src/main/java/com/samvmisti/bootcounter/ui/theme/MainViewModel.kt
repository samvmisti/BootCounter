package com.samvmisti.bootcounter.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samvmisti.bootcounter.data.MainDatabase
import com.samvmisti.bootcounter.data.model.TimestampEntity
import com.samvmisti.bootcounter.ui.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val database: MainDatabase
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun getTimestamps() = viewModelScope.launch {
        val timestamps = database.timestampDao().getAll().map(TimestampEntity::millis)
        _uiState.value = MainUiState(rebootTimestamps = timestamps)
    }
}
