package com.odogwudev.cowrywise.domain.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.odogwudev.cowrywise.data.model.Country
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CountriesUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Reads countries.json from assets and returns a List of Country objects.
     */
    fun loadCountries(): List<Country> {
        val jsonString = context.assets.open("countries.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}