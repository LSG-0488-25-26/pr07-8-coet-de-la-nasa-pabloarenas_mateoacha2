package com.example.coet_de_la_nasa.viewmodel

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
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getDogFeed(count)
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                result
                    .onSuccess { _dogs.value = it }
                    .onFailure { _error.value = it.message ?: "Error de red" }
            }
        }
    }
}

