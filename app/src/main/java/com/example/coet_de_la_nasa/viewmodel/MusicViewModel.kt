package com.example.coet_de_la_nasa.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coet_de_la_nasa.local.AppDatabase
import com.example.coet_de_la_nasa.local.SavedAlbum
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi
import com.example.coet_de_la_nasa.repository.MusicBrainzRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicBrainzRepository()
    private val dao = AppDatabase.getInstance(application).savedAlbumDao()

    private val _releaseGroups = MutableLiveData<List<ReleaseGroupItemUi>>(emptyList())
    val releaseGroups: LiveData<List<ReleaseGroupItemUi>> = _releaseGroups

    val savedAlbums: LiveData<List<SavedAlbum>> = dao.getAllLiveData()

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
                kotlinx.coroutines.delay(1100)
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

    fun addToCollection(mbid: String, title: String, artistName: String, coverUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(SavedAlbum(mbid = mbid, title = title, artistName = artistName, coverUrl = coverUrl))
        }
    }

    fun removeFromCollection(mbid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteByMbid(mbid)
        }
    }
}
