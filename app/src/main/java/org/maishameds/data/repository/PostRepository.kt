/*
 * Copyright 2020 MaishaMeds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.maishameds.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.maishameds.core.data.api.TypicodeAPI
import org.maishameds.core.data.network.PostsResponse
import org.maishameds.core.network.NetworkResult
import org.maishameds.core.network.flowSafeApiCall
import org.maishameds.data.dao.PostDao
import org.maishameds.data.mapper.toResponse
import org.maishameds.data.model.Post

class PostRepository(
    private val postDao: PostDao,
    private val typicodeAPI: TypicodeAPI,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun fetchPosts(): Flow<NetworkResult<List<PostsResponse>>> =
        flowSafeApiCall(ioDispatcher) {
            val postResponse = typicodeAPI.fetchPosts()
            val postLists = mutableListOf<Post>()
            postResponse.forEach {
                postLists.add(it.toResponse())
            }
            savePosts(postLists)
            return@flowSafeApiCall typicodeAPI.fetchPosts()
        }

    suspend fun savePosts(posts: List<Post>) =
        postDao.insert(posts)

    fun getPosts(): Flow<List<Post>> =
        postDao.getPosts()
}