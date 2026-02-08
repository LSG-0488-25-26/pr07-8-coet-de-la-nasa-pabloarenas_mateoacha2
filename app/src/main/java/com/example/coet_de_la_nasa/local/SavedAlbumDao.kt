package com.example.coet_de_la_nasa.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedAlbumDao {

    @Query("SELECT * FROM saved_albums ORDER BY title")
    fun getAllLiveData(): LiveData<List<SavedAlbum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: SavedAlbum)

    @Query("DELETE FROM saved_albums WHERE mbid = :mbid")
    suspend fun deleteByMbid(mbid: String)

    @Query("SELECT COUNT(*) FROM saved_albums WHERE mbid = :mbid")
    suspend fun isSaved(mbid: String): Int
}
