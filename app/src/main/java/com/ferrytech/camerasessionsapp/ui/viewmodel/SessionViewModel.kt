package com.ferrytech.camerasessionsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ferrytech.camerasessionsapp.data.model.SessionWithPhotos
import com.ferrytech.camerasessionsapp.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SessionRepository(application.applicationContext)

    private val _sessions = MutableStateFlow<List<com.ferrytech.camerasessionsapp.data.model.Session>>(emptyList())
    val sessions: StateFlow<List<com.ferrytech.camerasessionsapp.data.model.Session>>
        get() = _sessions

    private val _searchResults = MutableStateFlow<List<SessionWithPhotos>>(emptyList())
    val searchResults: StateFlow<List<SessionWithPhotos>>
        get() = _searchResults

    // Load all sessions from repository
    fun loadAllSessions() {
        viewModelScope.launch(Dispatchers.IO) {
            _sessions.value = repo.getAllSessions()
        }
    }

    // Create new session and notify caller with the new id
    fun createSession(name: String, age: Int, onCreated: (Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = repo.createSession(name, age)
            onCreated(id)
            loadAllSessions()
        }
    }

    // Add photo filename to a session
    fun addPhoto(sessionId: Long, fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addPhoto(sessionId, fileName)
        }
    }

    // Get details of session with photos
    fun getSessionDetails(sessionId: Long, onResult: (SessionWithPhotos?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getSessionWithPhotos(sessionId)
            onResult(result)
        }
    }

    // Search sessions by query string and age (if query is numeric)
    fun search(queryText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val query = queryText.trim()
            val age = query.toIntOrNull() ?: -1
            val result = repo.searchSessions(query, age)
            _searchResults.value = result
        }
    }
}
