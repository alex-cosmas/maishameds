package org.maishameds

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.maishameds.core.data.api.TypicodeAPI
import org.maishameds.data.Database
import org.maishameds.data.dao.PostDao
import org.maishameds.dispatcher.MockRequestDispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

internal open class BaseTest : BaseKoinTest() {

    // database and dao
    private lateinit var database: Database
    protected lateinit var postDao: PostDao

    // mock web server and network api
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var loggingInterceptor: HttpLoggingInterceptor
    lateinit var typicodeAPI: TypicodeAPI

    @Before
    open fun setup() {

        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room.inMemoryDatabaseBuilder(context, Database::class.java).allowMainThreadQueries()
                .build()
        postDao = database.postDao()

        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockRequestDispatcher()
        mockWebServer.start()
        loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        okHttpClient = buildOkhttpClient(loggingInterceptor)

        val gson = GsonBuilder()
            .serializeNulls()
            .create()

        typicodeAPI = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/post/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TypicodeAPI::class.java)
    }

    @After
    @Throws(IOException::class)
    open fun tearDown() {
        database.close()
        mockWebServer.shutdown()
    }

    private fun buildOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
}