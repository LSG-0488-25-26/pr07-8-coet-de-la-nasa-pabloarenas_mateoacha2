package com.example.coet_de_la_nasa.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedAlbumDao {

    @Query("SELECT * FROM saved_albums WHERE collectionId = :collectionId ORDER BY title")
    fun getByCollectionIdLiveData(collectionId: Long): LiveData<List<SavedAlbum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: SavedAlbum)

    @Query("DELETE FROM saved_albums WHERE mbid = :mbid AND collectionId = :collectionId")
    suspend fun deleteByMbidAndCollection(mbid: String, collectionId: Long)

    @Query("DELETE FROM saved_albums WHERE collectionId = :collectionId")
    suspend fun deleteByCollectionId(collectionId: Long)

    @Query("SELECT COUNT(*) FROM saved_albums WHERE mbid = :mbid")
    suspend fun isSaved(mbid: String): Int

    @Query("SELECT COUNT(*) FROM saved_albums WHERE collectionId = :collectionId")
    suspend fun countByCollectionId(collectionId: Long): Int
}
