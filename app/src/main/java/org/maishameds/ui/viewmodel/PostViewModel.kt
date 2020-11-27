
package org.maishameds.ui.viewmodel

import androidx.lifecycle.ViewModel
import org.maishameds.data.repository.PostRepository

class PostViewModel(
    private val postRepository: PostRepository
) : ViewModel() {

}