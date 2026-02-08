package com.example.coet_de_la_nasa.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "saved_albums",
    primaryKeys = ["mbid", "collectionId"],
    foreignKeys = [ForeignKey(
        entity = Collection::class,
        parentColumns = ["id"],
        childColumns = ["collectionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("collectionId")]
)
data class SavedAlbum(
    val mbid: String,
    val collectionId: Long,
    val title: String,
    val artistName: String,
    val coverUrl: String
)
