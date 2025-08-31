package com.ferrytech.camerasessionsapp.data.repository
import android.content.Context
import com.ferrytech.camerasessionsapp.data.AppDatabase
import com.ferrytech.camerasessionsapp.data.model.Photo
import com.ferrytech.camerasessionsapp.data.model.Session
import com.ferrytech.camerasessionsapp.data.model.SessionWithPhotos
class SessionRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val dao = db.sessionDao()
    suspend fun createSession(name: String, age: Int): Long {
        val session = Session(name = name, age = age)
        return dao.insertSession(session)
    }
    suspend fun addPhoto(sessionId: Long, fileName: String): Long {
        val photo = Photo(sessionId = sessionId, fileName = fileName)
        return dao.insertPhoto(photo)
    }
    suspend fun getSessionWithPhotos(sessionId: Long): SessionWithPhotos? =
        dao.getSessionWithPhotos(sessionId)
    suspend fun searchSessions(query: String, ageQuery: Int):
            List<SessionWithPhotos> = dao.searchSessions(query, ageQuery)
    suspend fun getAllSessions() = dao.getAllSessions()
}