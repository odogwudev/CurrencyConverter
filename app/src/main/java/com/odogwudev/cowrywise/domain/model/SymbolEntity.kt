package com.odogwudev.cowrywise.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "symbols")
data class SymbolEntity(
    @PrimaryKey val code: String, val name: String
)