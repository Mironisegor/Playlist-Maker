package com.example.playlistmaker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.playlistmaker.database.AppDatabase
import com.example.playlistmaker.domain.ITunesApiService
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.network.TracksRepositoryImpl
import com.example.playlistmaker.data.network.PlaylistsRepositoryImpl
import com.example.playlistmaker.data.preferences.SearchHistoryPreferences
import com.example.playlistmaker.data.preferences.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.TracksRepository
import com.example.playlistmaker.domain.PlaylistsRepository
import com.example.playlistmaker.domain.SearchHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_history_preferences")

private const val ITUNES_BASE_URL = "https://itunes.apple.com/"

object Creator {
    @Volatile
    private var database: AppDatabase? = null
    
    @Volatile
    private var dataStore: DataStore<Preferences>? = null

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        builder.addInterceptor(logging)
        
        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val itunesApiService: ITunesApiService by lazy {
        retrofit.create(ITunesApiService::class.java)
    }

    private val networkClient: RetrofitNetworkClient by lazy {
        RetrofitNetworkClient(itunesApiService)
    }

    fun initDatabase(context: Context) {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    database = AppDatabase.getInstance(context)
                }
            }
        }
        if (dataStore == null) {
            synchronized(this) {
                if (dataStore == null) {
                    dataStore = context.dataStore
                }
            }
        }
    }

    private fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("Database not initialized. Call initDatabase(context) first.")
    }

    fun getTracksRepository(scope: CoroutineScope): TracksRepository {
        return TracksRepositoryImpl(
            scope = scope,
            networkClient = networkClient,
            database = getDatabase()
        )
    }

    fun getTracksRepository(): TracksRepository {
        return getTracksRepository(CoroutineScope(Dispatchers.IO))
    }

    fun getPlaylistsRepository(scope: CoroutineScope): PlaylistsRepository {
        return PlaylistsRepositoryImpl(
            scope = scope,
            database = getDatabase()
        )
    }

    fun getSearchHistoryRepository(): SearchHistoryRepository {
        val dataStoreInstance = dataStore ?: throw IllegalStateException("DataStore not initialized. Call initDatabase(context) first.")
        val searchHistoryPreferences = SearchHistoryPreferences(dataStoreInstance)
        return SearchHistoryRepositoryImpl(searchHistoryPreferences)
    }
}

