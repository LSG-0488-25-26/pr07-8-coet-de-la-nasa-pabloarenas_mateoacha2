package com.example.coet_de_la_nasa.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

data class CollectionWithCount(
    val id: Long,
    val name: String,
    val albumCount: Int
)

@Dao
interface CollectionDao {

    @Query("SELECT * FROM collections ORDER BY name")
    fun getAllLiveData(): LiveData<List<Collection>>

    @Query("SELECT id, name, (SELECT COUNT(*) FROM saved_albums WHERE collectionId = collections.id) AS albumCount FROM collections ORDER BY name")
    fun getAllWithCountLiveData(): LiveData<List<CollectionWithCount>>

    @Insert
    suspend fun insert(collection: Collection): Long

    @Query("DELETE FROM collections WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM collections WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): Collection?

    @Query("SELECT * FROM collections WHERE id = :id LIMIT 1")
    fun getByIdLiveData(id: Long): LiveData<Collection?>
}
