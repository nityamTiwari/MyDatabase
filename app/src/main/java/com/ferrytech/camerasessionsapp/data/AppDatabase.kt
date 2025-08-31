package com.ferrytech.camerasessionsapp.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ferrytech.camerasessionsapp.data.dao.SessionDao
import com.ferrytech.camerasessionsapp.data.model.Photo
import com.ferrytech.camerasessionsapp.data.model.Session
@Database(entities = [Session::class, Photo::class], version = 1, exportSchema
= false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "camera_sessions_db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
