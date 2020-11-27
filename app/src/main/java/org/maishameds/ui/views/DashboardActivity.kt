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
package org.maishameds.ui.views

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.maishameds.R
import org.maishameds.core.network.NetworkResult
import org.maishameds.data.mapper.toResponse
import org.maishameds.data.model.Post
import org.maishameds.databinding.ActivityDashboardBinding
import org.maishameds.ui.adapters.PostsAdapter
import org.maishameds.ui.viewmodel.PostViewModel

class DashboardActivity : BindingActivity<ActivityDashboardBinding>() {

    private lateinit var postsAdapter: PostsAdapter
    private val postViewModel: PostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        binding.appBarLayout.title.text = getString(R.string.app_name)

        postsAdapter = PostsAdapter()
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.recyclerView.adapter = postsAdapter

        lifecycleScope.launch {
            postViewModel.fetchPosts().collect {
                when (it) {
                    is NetworkResult.Success -> {
                        showSnackbar("Fetched data from Typicode API")
                        val postLists = mutableListOf<Post>()
                        it.data.forEach {
                            postLists.add(it.toResponse())
                        }
                        postViewModel.savePosts(postLists)
                    }
                    is NetworkResult.ServerError -> {
                        showSnackbar(it.errorBody?.message ?: "A network error occurred")
                    }
                    is NetworkResult.NetworkError -> {
                        showSnackbar("A network error occurred when making your request")
                    }
                }
            }
        }

        postViewModel.getPosts().observe(
            this,
            {
                postsAdapter.submitList(it)
            }
        )
    }

    override val layoutResId: Int
        get() = R.layout.activity_dashboard
}