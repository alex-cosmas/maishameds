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

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.maishameds.BaseTest
import org.maishameds.core.network.NetworkResult
import org.maishameds.data.sample.testPosts
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(
    sdk = [Config.OLDEST_SDK],
    manifest = Config.NONE
) // https://stackoverflow.com/questions/56821193/does-robolectric-require-java-9
internal class PostRepositoryTest : BaseTest() {

    private lateinit var postRepository: PostRepository

    @Before
    fun setUp() {
        super.setup()
        postRepository = PostRepository(postDao, typicodeAPI)
    }

    @Test
    fun `test fetching posts from API`() {
        runBlocking {
            val postResponse =
                postRepository.fetchPosts()

            postResponse.collect {
                Truth.assertThat(it).isInstanceOf(NetworkResult.Success::class.java)
                (it as NetworkResult.Success)
                Truth.assertThat(it.data[0].title)
                    .matches("sunt aut facere repellat provident occaecati excepturi optio reprehenderit")
            }
        }
    }

    @Test
    fun `test inserting and fetching posts`() {
        runBlocking {
            postRepository.savePosts(testPosts)
            val post = postRepository.getPosts().first().toList()[0]
            MatcherAssert.assertThat(post.postBody, `is`(testPosts[0].postBody))
        }
    }
}