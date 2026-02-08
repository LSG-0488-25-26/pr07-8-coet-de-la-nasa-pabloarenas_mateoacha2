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

    private val _currentQuery = MutableLiveData("")
    val currentQuery: LiveData<String> = _currentQuery

    fun setQuery(query: String) {
        _currentQuery.value = query
    }

    fun search(query: String, limit: Int = 20) {
        if (query.isBlank()) return
        _currentQuery.value = query
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
                        .onFailure { _error.value = friendlyMessage(it.message) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _error.value = friendlyMessage(e.message)
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

    private fun friendlyMessage(raw: String?): String {
        if (raw.isNullOrBlank()) return "Error de connexio"
        return when {
            raw.contains("Connection reset", ignoreCase = true) ->
                "La connexio s'ha tancat. Comprova el Wi-Fi o les dades i torna a intentar."
            raw.contains("timeout", ignoreCase = true) ->
                "Temps esgotat. Comprova la connexio i torna a intentar."
            raw.contains("Unable to resolve host", ignoreCase = true) ->
                "Sense connexio a Internet. Comprova la xarxa."
            else -> raw
        }
    }
}
