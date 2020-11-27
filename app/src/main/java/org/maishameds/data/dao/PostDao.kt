package org.maishameds.data.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.maishameds.data.model.Post

@Dao
interface PostDao : BaseDao<Post> {

    @Query("SELECT * FROM Post")
    fun getPosts(): Flow<List<Post>>
}