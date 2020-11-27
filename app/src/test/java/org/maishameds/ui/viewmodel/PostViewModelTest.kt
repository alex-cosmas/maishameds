package org.maishameds.ui.viewmodel

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.maishameds.BaseViewModelTest
import org.maishameds.data.repository.PostRepository
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

        coEvery {
            postRepository.fetchPosts()
        } returns flowOf()

    }
}