package com.example.coet_de_la_nasa.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coet_de_la_nasa.model.DogItemUi
import com.example.coet_de_la_nasa.repository.DogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DogViewModel(
    private val repository: DogRepository = DogRepository()
) : ViewModel() {

    private val _dogs = MutableLiveData<List<DogItemUi>>(emptyList())
    val dogs: LiveData<List<DogItemUi>> = _dogs

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadDogs()
    }

    fun loadDogs(count: Int = 30) {
        Log.d("DogViewModel", "loadDogs called with count=$count")
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("DogViewModel", "Starting API call...")
            val result = repository.getDogFeed(count)
            Log.d("DogViewModel", "API call completed: ${if (result.isSuccess) "SUCCESS" else "FAILURE"}")
            
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                result
                    .onSuccess { 
                        Log.d("DogViewModel", "Success! Received ${it.size} dogs")
                        _dogs.value = it 
                    }
                    .onFailure { 
                        val errorMsg = it.message ?: "Error de red"
                        Log.e("DogViewModel", "Error loading dogs: $errorMsg", it)
                        _error.value = errorMsg
                    }
            }
        }
    }
}

