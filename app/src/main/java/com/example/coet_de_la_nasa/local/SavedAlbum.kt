package com.example.coet_de_la_nasa.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_albums")
data class SavedAlbum(
    @PrimaryKey val mbid: String,
    val title: String,
    val artistName: String,
    val coverUrl: String
)
