package com.example.coet_de_la_nasa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coet_de_la_nasa.model.LeagueItemUi
import com.example.coet_de_la_nasa.repository.FootballRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FootballViewModel(
    private val repository: FootballRepository = FootballRepository()
) : ViewModel() {

    private val _leagues = MutableLiveData<List<LeagueItemUi>>(emptyList())
    val leagues: LiveData<List<LeagueItemUi>> = _leagues

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadLeagues()
    }

    fun loadLeagues() {
        Log.d("FootballViewModel", "loadLeagues called")
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("FootballViewModel", "Starting API call...")
            val result = repository.getLeagues()
            Log.d("FootballViewModel", "API call completed: ${if (result.isSuccess) "SUCCESS" else "FAILURE"}")
            
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                result
                    .onSuccess { 
                        Log.d("FootballViewModel", "Success! Received ${it.size} leagues")
                        _leagues.value = it 
                    }
                    .onFailure { 
                        val errorMsg = it.message ?: "Error de red"
                        Log.e("FootballViewModel", "Error loading leagues: $errorMsg", it)
                        _error.value = errorMsg
                    }
            }
        }
    }
}
