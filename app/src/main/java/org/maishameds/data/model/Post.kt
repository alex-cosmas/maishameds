package org.maishameds.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Post(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var postID: Int,
    var postTitle: String,
    var postBody: String
)