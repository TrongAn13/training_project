package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.database.dao.FavoriteMovieDAO
import com.example.database.dao.MovieDAO
import com.example.database.dao.SearchHistoryDAO
import com.example.database.entity.FavoriteMovieEntity
import com.example.database.entity.MovieEntity
import com.example.database.entity.SearchHistoryEntity

@Database(entities = [MovieEntity::class, SearchHistoryEntity::class, FavoriteMovieEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDAO
    abstract fun  searchHistoryDao(): SearchHistoryDAO

    abstract fun favoriteMovieDao(): FavoriteMovieDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movie_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3,MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE movies ADD COLUMN page INTEGER NOT NULL DEFAULT 1"
                )

                db.execSQL(
                    "ALTER TABLE movies ADD COLUMN position INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS search_history (
                id INTEGER NOT NULL PRIMARY KEY,
                title TEXT NOT NULL,
                poster_path TEXT NOT NULL,
                vote_average REAL NOT NULL,
                release_date TEXT NOT NULL,
                genres TEXT NOT NULL,
                overview TEXT NOT NULL,
                savedAt INTEGER NOT NULL
            )
        """.trimIndent())
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
            CREATE TABLE IF NOT EXISTS favorite_movies (
                id INTEGER NOT NULL PRIMARY KEY,
                title TEXT NOT NULL,
                poster_path TEXT NOT NULL,
                vote_average REAL NOT NULL,
                release_date TEXT NOT NULL,
                genres TEXT NOT NULL,
                overview TEXT NOT NULL,
                savedAt INTEGER NOT NULL,
                detailViewCount INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent())
            }
        }

    }
}