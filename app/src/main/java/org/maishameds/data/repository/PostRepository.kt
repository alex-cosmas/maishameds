package org.maishameds.data.repository

import org.maishameds.core.data.api.TypicodeAPI
import org.maishameds.data.dao.PostDao

class PostRepository(
    private val postDao: PostDao,
    private val typicodeAPI: TypicodeAPI,
) {

}