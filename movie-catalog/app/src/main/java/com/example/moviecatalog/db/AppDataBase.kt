package com.example.moviecatalog.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moviecatalog.model.Element

/**
 * Code obtained from here:
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#7
 */
@Database(entities = [Element::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun elementDAO(): ElementDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "movie-catalog-db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
