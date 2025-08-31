package com.ferrytech.camerasessionsapp.data.dao


import androidx.room.*

import com.ferrytech.camerasessionsapp.data.model.Photo
import com.ferrytech.camerasessionsapp.data.model.Session
import com.ferrytech.camerasessionsapp.data.model.SessionWithPhotos

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: Session): Long
    @Insert
    suspend fun insertPhoto(photo: Photo): Long
    @Transaction
    @Query("SELECT * FROM sessions WHERE sessionId = :id")
    suspend fun getSessionWithPhotos(id: Long): SessionWithPhotos?
    @Transaction
    @Query("SELECT * FROM sessions WHERE name LIKE '%' || :query || '%' OR age = :ageQuery ORDER BY createdAt DESC")

                suspend fun searchSessions(query: String, ageQuery: Int):
                List<SessionWithPhotos>
        @Query("SELECT * FROM sessions ORDER BY createdAt DESC")
        suspend fun getAllSessions(): List<Session>
}