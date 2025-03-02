package com.odogwudev.cowrywise.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.odogwudev.cowrywise.domain.model.FixerApi
import com.odogwudev.cowrywise.domain.model.FixerDatabase
import com.odogwudev.cowrywise.domain.repository.CountriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://data.fixer.io/api/"
    private const val DB_NAME = "fixer_db"


    @Provides
    @Singleton
    @JvmStatic
    internal fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder().apply {
            readTimeout(30, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
        }.addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                println("LOG-APP: $message")
            }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addNetworkInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                println("LOG-NET: $message")
            }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun provideFixerApi(retrofit: Retrofit): FixerApi {
        return retrofit.create(FixerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFixerDatabase(@ApplicationContext context: Context): FixerDatabase {
        return Room.databaseBuilder(
            context, FixerDatabase::class.java, DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCountriesUseCase(@ApplicationContext context: Context): CountriesUseCase {
        return CountriesUseCase(context)
    }

    @Provides
    fun provideExchangeRateDao(db: FixerDatabase) = db.exchangeRateDao()

    @Provides
    fun provideSymbolsDao(db: FixerDatabase) = db.symbolsDao()

    @Provides
    fun provideTimeSeriesDao(db: FixerDatabase) = db.timeSeriesDao()
}