package com.odogwudev.cowrywise.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val currencyCode: String,
    val rate: Double,
    val base: String,
    val date: String
)