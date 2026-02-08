package com.example.coet_de_la_nasa.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coet_de_la_nasa.local.AppDatabase
import com.example.coet_de_la_nasa.local.Collection
import com.example.coet_de_la_nasa.local.CollectionWithCount
import com.example.coet_de_la_nasa.local.SavedAlbum
import com.example.coet_de_la_nasa.model.ReleaseGroupDetailUi
import com.example.coet_de_la_nasa.model.ReleaseGroupItemUi
import com.example.coet_de_la_nasa.repository.MusicBrainzRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicBrainzRepository()
    private val savedAlbumDao = AppDatabase.getInstance(application).savedAlbumDao()
    private val collectionDao = AppDatabase.getInstance(application).collectionDao()

    private val _releaseGroups = MutableLiveData<List<ReleaseGroupItemUi>>(emptyList())
    val releaseGroups: LiveData<List<ReleaseGroupItemUi>> = _releaseGroups

    val collectionsWithCount: LiveData<List<CollectionWithCount>> = collectionDao.getAllWithCountLiveData()

    fun getAlbumsForCollection(collectionId: Long): LiveData<List<SavedAlbum>> =
        savedAlbumDao.getByCollectionIdLiveData(collectionId)

    fun getCollection(collectionId: Long): LiveData<Collection?> = collectionDao.getByIdLiveData(collectionId)

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _currentQuery = MutableLiveData("")
    val currentQuery: LiveData<String> = _currentQuery

    private val _releaseGroupDetail = MutableLiveData<ReleaseGroupDetailUi?>(null)
    val releaseGroupDetail: LiveData<ReleaseGroupDetailUi?> = _releaseGroupDetail

    private val _detailLoading = MutableLiveData(false)
    val detailLoading: LiveData<Boolean> = _detailLoading

    private val _detailError = MutableLiveData<String?>(null)
    val detailError: LiveData<String?> = _detailError

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

    fun addToCollection(mbid: String, title: String, artistName: String, coverUrl: String, collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            savedAlbumDao.insert(SavedAlbum(mbid = mbid, collectionId = collectionId, title = title, artistName = artistName, coverUrl = coverUrl))
        }
    }

    fun removeFromCollection(mbid: String, collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            savedAlbumDao.deleteByMbidAndCollection(mbid, collectionId)
        }
    }

    fun addCollection(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.insert(Collection(name = name))
        }
    }

    fun deleteCollection(collectionId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionDao.deleteById(collectionId)
        }
    }

    fun loadDetail(mbid: String) {
        _releaseGroupDetail.value = null
        _detailError.value = null
        _detailLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                kotlinx.coroutines.delay(1100)
                val result = repository.lookupReleaseGroup(mbid)
                withContext(Dispatchers.Main) {
                    _detailLoading.value = false
                    result
                        .onSuccess { _releaseGroupDetail.value = it }
                        .onFailure { _detailError.value = friendlyMessage(it.message) }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _detailLoading.value = false
                    _detailError.value = friendlyMessage(e.message)
                }
            }
        }
    }

    fun clearDetail() {
        _releaseGroupDetail.value = null
        _detailError.value = null
    }

    private fun friendlyMessage(raw: String?): String {
        if (raw.isNullOrBlank()) return "No se han podido cargar los detalles. Reintenta."
        return when {
            raw.contains("Connection reset", ignoreCase = true) ->
                "La conexión se ha cerrado. Comprueba el Wi-Fi o los datos y vuelve a intentar."
            raw.contains("timeout", ignoreCase = true) ->
                "Tiempo agotado. Comprueba la conexión y vuelve a intentar."
            raw.contains("Unable to resolve host", ignoreCase = true) ->
                "Sin conexión a Internet. Comprueba la red."
            raw.contains("HTTP 503", ignoreCase = true) ->
                "Servidor ocupado. Espera un momento y pulsa REINTENTAR."
            raw.contains("Expected ", ignoreCase = true) || raw.contains("Json", ignoreCase = true) ->
                "Error al interpretar los datos. Reintenta más tarde."
            else -> raw
        }
    }
}
