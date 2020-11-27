package org.maishameds.data

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.maishameds.data.dao.PostDao
import org.maishameds.data.model.Post

@androidx.room.Database(
    entities = [
        Post::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun postDao(): PostDao
}