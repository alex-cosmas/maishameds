package org.maishameds.data.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.maishameds.BaseTest
import org.maishameds.data.sample.testPosts
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK], manifest = Config.NONE) // https://stackoverflow.com/questions/56821193/does-robolectric-require-java-9
internal class PostDaoTest : BaseTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Test
    fun `test inserting and retrieving posts`() = runBlockingTest {
        postDao.insert(testPosts)
        val post = postDao.getPosts().first().toList()[0]
        assertThat(post.postTitle, `is`(testPosts[0].postTitle))
    }
}