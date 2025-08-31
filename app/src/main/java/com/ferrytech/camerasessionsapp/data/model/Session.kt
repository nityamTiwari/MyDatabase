package com.ferrytech.camerasessionsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "sessions")
data class Session(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val name: String,
    val age: Int,
    val createdAt: Long = System.currentTimeMillis()
)
