package com.ferrytech.camerasessionsapp.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey(autoGenerate = true) val photoId: Long = 0,
    val sessionId: Long,
    val fileName: String,
    val addedAt: Long = System.currentTimeMillis()
)
