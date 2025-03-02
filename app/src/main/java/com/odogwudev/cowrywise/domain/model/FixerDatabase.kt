package com.odogwudev.cowrywise.domain.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.odogwudev.cowrywise.data.model.ExchangeRateEntity

@Database(
    entities = [ExchangeRateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FixerDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun symbolsDao(): SymbolsDao
    abstract fun timeSeriesDao(): TimeSeriesDao
}