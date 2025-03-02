package com.odogwudev.cowrywise.presenntation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.odogwudev.cowrywise.presenntation.viewmodel.FixerViewModel

@Composable
fun FixerRatesScreen(
    modifier: Modifier = Modifier,
    fixerViewModel: FixerViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        fixerViewModel.loadLatestRates("Usd","Usd")
    }

}