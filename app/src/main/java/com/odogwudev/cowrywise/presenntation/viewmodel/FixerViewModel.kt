package com.odogwudev.cowrywise.presenntation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odogwudev.cowrywise.data.model.Country
import com.odogwudev.cowrywise.data.model.ExchangeRateEntity
import com.odogwudev.cowrywise.data.repository.FixerRepository
import com.odogwudev.cowrywise.data.repository.Resource
import com.odogwudev.cowrywise.domain.model.SymbolEntity
import com.odogwudev.cowrywise.domain.repository.CountriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FixerViewModel @Inject constructor(
    private val repository: FixerRepository,
    private val countriesUseCase: CountriesUseCase
) : ViewModel() {

    private val _countries = MutableStateFlow<List<Country>>(emptyList())
    val countries = _countries.asStateFlow()

    init {
        loadCountries()
    }

    fun loadCountries() {
        viewModelScope.launch {
            try {
                val list = countriesUseCase.loadCountries()
                _countries.value = list
            } catch (e: Exception) {
                Log.e("FixerViewModel", "Failed to load countries: ${e.localizedMessage}", e)
                // You can handle the error in your own way, e.g.:
                // _countries.value = emptyList()
            }
        }
    }



    private val _ratesState = MutableStateFlow<Resource<List<ExchangeRateEntity>>>(Resource.Loading)
    val ratesState = _ratesState.asStateFlow()

    fun loadLatestRates(base: String,symbols: String) {
        viewModelScope.launch {
            repository.getLatestRates(base,symbols).collectLatest { resource ->
                _ratesState.value = resource
            }
        }
    }

    private val _convertResultLocally = MutableStateFlow<Resource<Double>>(Resource.Success(0.0))
    val convertResultLocally: StateFlow<Resource<Double>> = _convertResultLocally

    fun convertRatesLocally(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            repository.convertLocally(from, to, amount).collect { result ->
                _convertResultLocally.value = result
            }
        }
    }
    private val _symbolsState = MutableStateFlow<Resource<List<SymbolEntity>>>(Resource.Loading)
    val symbolsState = _symbolsState.asStateFlow()

    fun loadSymbols() {
        viewModelScope.launch {
            repository.getSymbols().collect { resource ->
                _symbolsState.value = resource
            }
        }
    }

    private val _convertResult = MutableStateFlow<Resource<Double>>(Resource.Success(0.0))
    val convertResult: StateFlow<Resource<Double>> = _convertResult

    // 2) Convert function
    fun convertRates(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            repository.convert(from, to, amount).collect { result ->
                _convertResult.value = result
            }
        }
    }

}