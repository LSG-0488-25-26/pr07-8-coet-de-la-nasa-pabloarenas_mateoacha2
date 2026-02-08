package com.example.coet_de_la_nasa.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [SavedAlbum::class, Collection::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedAlbumDao(): SavedAlbumDao
    abstract fun collectionDao(): CollectionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS collections (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)")
                db.execSQL("INSERT INTO collections (id, name) VALUES (1, 'Mi colección')")
                db.execSQL("CREATE TABLE IF NOT EXISTS saved_albums_new (mbid TEXT NOT NULL, collectionId INTEGER NOT NULL, title TEXT NOT NULL, artistName TEXT NOT NULL, coverUrl TEXT NOT NULL, PRIMARY KEY(mbid, collectionId), FOREIGN KEY(collectionId) REFERENCES collections(id) ON DELETE CASCADE)")
                db.execSQL("INSERT INTO saved_albums_new (mbid, collectionId, title, artistName, coverUrl) SELECT mbid, 1, title, artistName, coverUrl FROM saved_albums")
                db.execSQL("DROP TABLE saved_albums")
                db.execSQL("ALTER TABLE saved_albums_new RENAME TO saved_albums")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_saved_albums_collectionId ON saved_albums (collectionId)")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coet_nasa_db"
                ).addMigrations(MIGRATION_1_2).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("INSERT INTO collections (id, name) VALUES (1, 'Mi colección')")
                    }
                }).build().also { INSTANCE = it }
            }
        }
    }
}
