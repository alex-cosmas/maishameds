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
package org.maishameds.core.network

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class NetworkResult<out R> {

    /**
     * This is used to represent successful responses (2xx response codes, non empty response bodies)
     */
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /**
     * Used to represent Server errors (non 2xx status code)
     */
    data class ServerError(
        val code: Int? = null,
        val errorBody: ErrorResponse? = null
    ) : NetworkResult<Nothing>()

    /**
     * Used to represent connectivity errors (a request that didn't result in a response)
     */
    object NetworkError : NetworkResult<Nothing>()
}