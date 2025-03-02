package com.odogwudev.cowrywise.domain.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.odogwudev.cowrywise.data.model.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<ExchangeRateEntity>)

    @Query("SELECT * FROM exchange_rates WHERE base = :base")
    fun getRatesByBase(base: String): Flow<List<ExchangeRateEntity>>

    @Query("SELECT * FROM exchange_rates WHERE base = :base")
    suspend fun getRatesByBaseOnce(base: String): List<ExchangeRateEntity>

    @Query("DELETE FROM exchange_rates WHERE base = :base")
    suspend fun deleteRatesByBase(base: String)

    @Query("SELECT * FROM exchange_rates WHERE base = :base AND currencyCode = :currency LIMIT 1")
    suspend fun getRateByCurrency(base: String, currency: String): ExchangeRateEntity?
}