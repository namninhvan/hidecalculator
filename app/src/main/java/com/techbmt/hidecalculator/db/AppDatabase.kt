package com.techbmt.hidecalculator.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.techbmt.hidecalculator.db.entities.Note
import com.techbmt.hidecalculator.feature.main.images.HiddenFile
import com.techbmt.hidecalculator.feature.main.video.VideoModel

@Database(entities = [Note::class, HiddenFile::class, VideoModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAppDAO(): AppDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calculator.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}