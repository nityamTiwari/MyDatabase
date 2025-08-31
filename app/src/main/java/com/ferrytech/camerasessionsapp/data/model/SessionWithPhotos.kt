package com.ferrytech.camerasessionsapp.data.model

import androidx.room.Embedded
import androidx.room.Relation
data class SessionWithPhotos(
    @Embedded val session: Session,
    @Relation(parentColumn = "sessionId", entityColumn = "sessionId")
    val photos: List<Photo>
)