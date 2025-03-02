package com.odogwudev.cowrywise.domain.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SymbolsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSymbols(symbols: List<SymbolEntity>)

    @Query("SELECT * FROM symbols")
    fun getAllSymbols(): Flow<List<SymbolEntity>>

    @Query("DELETE FROM symbols")
    suspend fun clearAllSymbols()
}
@Dao
interface TimeSeriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeSeriesRates(symbols: List<TimeSeriesRateEntity>)

    @Query("SELECT * FROM symbols")
    fun getAllTimeSeriesRates(): Flow<List<TimeSeriesRateEntity>>

    @Query("DELETE FROM symbols")
    suspend fun clearAllTimeSeriesRates()
}