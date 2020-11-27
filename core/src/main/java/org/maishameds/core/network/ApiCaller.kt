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

import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): NetworkResult<T> = withContext(dispatcher) {
    try {
        NetworkResult.Success(apiCall.invoke())
    } catch (throwable: Throwable) {
        Timber.e(throwable)
        when (throwable) {
            is IOException -> NetworkResult.NetworkError
            is HttpException -> {
                val code = throwable.code()

                val errorResponse = convertErrorBody(throwable)
                NetworkResult.ServerError(code, errorResponse)
            }
            else -> {
                NetworkResult.ServerError(null, null)
            }
        }
    }
}

suspend fun <T> flowSafeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Flow<NetworkResult<T>> = flow {
    try {
        emit(NetworkResult.Success(apiCall.invoke()))
    } catch (throwable: Throwable) {
        Timber.e(throwable)
        when (throwable) {
            is IOException -> emit(NetworkResult.NetworkError)
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = convertErrorBody(throwable)
                emit(NetworkResult.ServerError(code, errorResponse))
            }
            else -> {
                emit(NetworkResult.ServerError(null, null))
            }
        }
    }
}.flowOn(dispatcher)

private fun convertErrorBody(throwable: HttpException): ErrorResponse? = try {
    throwable.response()?.errorBody()?.charStream()?.let {
        val gson = GsonBuilder()
            .create()
        gson.fromJson(it, ErrorResponse::class.java)
    }
} catch (exception: Exception) {
    Timber.e(exception)
    null
}

inline fun <T> getResult(
    response: Response<List<T>>,
    onError: () -> NetworkResult.ServerError
): NetworkResult<List<T>> {
    if (response.isSuccessful) {
        val body = response.body()
        if (body != null) {
            return NetworkResult.Success(body)
        }
    } else {
        Timber.e(response.errorBody()?.string())
    }
    return onError.invoke()
}