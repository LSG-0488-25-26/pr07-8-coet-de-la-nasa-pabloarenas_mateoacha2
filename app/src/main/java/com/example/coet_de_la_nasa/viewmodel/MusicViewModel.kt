package com.example.coet_de_la_nasa.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi
import com.example.coet_de_la_nasa.repository.MusicBrainzRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicViewModel(
    private val repository: MusicBrainzRepository = MusicBrainzRepository()
) : ViewModel() {

    private val _releaseGroups = MutableLiveData<List<ReleaseGroupItemUi>>(emptyList())
    val releaseGroups: LiveData<List<ReleaseGroupItemUi>> = _releaseGroups

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        search("rock")
    }

    fun search(query: String, limit: Int = 20) {
        if (query.isBlank()) return
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                kotlinx.coroutines.delay(1100) // MusicBrainz: 1 req/sec
                val result = repository.searchReleaseGroups(query = query, limit = limit)
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    result
                        .onSuccess { _releaseGroups.value = it }
                        .onFailure { _error.value = it.message ?: "Error de red" }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _error.value = e.message ?: "Error de conexión"
                }
            }
        }
    }
}
