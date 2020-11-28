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
package org.maishameds.ui.viewmodel

import com.jraska.livedata.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.maishameds.BaseViewModelTest
import org.maishameds.core.network.NetworkResult
import org.maishameds.data.repository.PostRepository
import org.maishameds.data.sample.testPostsResponse
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
class PostViewModelTest : BaseViewModelTest() {

    private val postRepository = mockk<PostRepository>()

    private lateinit var postViewModel: PostViewModel

    @Before
    fun setUp() {
        postViewModel = PostViewModel(postRepository)
    }

    @FlowPreview
    @Test
    fun `test fetch posts works successfully`() {
        runBlocking {
            coEvery { postRepository.fetchPosts() } returns flowOf(
                NetworkResult.Success(
                    testPostsResponse
                )
            )
            postViewModel.fetchPosts()
            coVerify { postRepository.fetchPosts() }
            postViewModel.postsResponse.test().assertValue(NetworkResult.Success(testPostsResponse))
        }
    }
}