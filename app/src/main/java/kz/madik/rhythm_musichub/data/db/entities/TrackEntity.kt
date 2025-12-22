package kz.madik.rhythm_musichub.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val album: String?,
    val duration: Long,
    val audioUrl: String,
    val coverUrl: String?,
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)

