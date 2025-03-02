package com.odogwudev.cowrywise.di

import android.content.Context
import androidx.room.Room
import com.odogwudev.cowrywise.domain.model.FixerApi
import com.odogwudev.cowrywise.domain.model.FixerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://data.fixer.io/api/"
    private const val DB_NAME = "fixer_db"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
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
    fun provideExchangeRateDao(db: FixerDatabase) = db.exchangeRateDao()

    @Provides
    fun provideSymbolsDao(db: FixerDatabase) = db.symbolsDao()

    @Provides
    fun provideTimeSeriesDao(db: FixerDatabase) = db.timeSeriesDao()
}