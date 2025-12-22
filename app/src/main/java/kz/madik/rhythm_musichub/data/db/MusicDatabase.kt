package kz.madik.rhythm_musichub.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.madik.rhythm_musichub.data.db.dao.TrackDao
import kz.madik.rhythm_musichub.data.db.entities.TrackEntity

@Database(
    entities = [TrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    companion object {
        @Volatile
        private var INSTANCE: MusicDatabase? = null

        fun getDatabase(context: Context): MusicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    "music_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

