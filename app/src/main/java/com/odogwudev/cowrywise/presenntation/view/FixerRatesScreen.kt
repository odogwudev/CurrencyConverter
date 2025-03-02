package com.odogwudev.cowrywise.presenntation.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.odogwudev.cowrywise.R
import com.odogwudev.cowrywise.data.model.Country
import com.odogwudev.cowrywise.data.model.ExchangeRateEntity
import com.odogwudev.cowrywise.data.repository.Resource
import com.odogwudev.cowrywise.presenntation.view.components.SelectableItemCard
import com.odogwudev.cowrywise.presenntation.view.components.SelectionTitleListOnlyContent
import com.odogwudev.cowrywise.presenntation.viewmodel.FixerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixerRatesScreen(
    modifier: Modifier = Modifier, fixerViewModel: FixerViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        fixerViewModel.loadLatestRates(base = "", symbols = "USD")
    }

    val countries by fixerViewModel.countries.collectAsState()

    val convertResult by fixerViewModel.ratesState.collectAsState()

    var showParentCountry by rememberSaveable { mutableStateOf(false) }
    var showSubCountry by rememberSaveable { mutableStateOf(false) }
    var showMidMonthRate by rememberSaveable { mutableStateOf(false) }

    var selectedParentCountry by remember {
        mutableStateOf<Country?>(
            Country(
                currencyId = "EUR",
                currencyName = "Euro",
                countryCode = "EU",
                currencySymbol = "€",
                flagUrl = "https://flagcdn.com/eu.svg"
            )
        )
    }
    var selectedSubCountry by remember {
        mutableStateOf<Country?>(
            Country(
                currencyId = "USD",
                currencyName = "United States Dollar",
                countryCode = "US",
                currencySymbol = "$",
                flagUrl = "https://flagcdn.com/us.svg"
            )
        )
    }

    var fromValue by rememberSaveable { mutableStateOf("") }

    var toValue by remember { mutableStateOf("") }

    when (convertResult) {
        is Resource.Loading -> toValue= "Loading..."
        is Resource.Error ->toValue ="Error: ${(convertResult as Resource.Error).message}"
        is Resource.Success -> {
            val entity = (convertResult as Resource.Success<List<ExchangeRateEntity>>).data.firstOrNull()
            if (entity != null) {
                val fromDouble = fromValue.toDoubleOrNull() ?: 0.0
                val final = entity.rate * fromDouble
                toValue= final.toString()
            } else {
                toValue ="Empty list from DB"
            }
        }
    }
    LaunchedEffect(Unit) {
        val localResult = convertResult
        when (val localVal = convertResult) {
            is Resource.Success -> {
                // localVal.data is List<ExchangeRateEntity>
                val entity = localVal.data.firstOrNull()
                if (entity != null) {
                    val fromDouble = fromValue.toDoubleOrNull() ?: 0.0
                    val final = entity.rate * fromDouble
                    toValue = final.toString()
                } else {
                    toValue = "Error (empty list)"
                }
            }
            is Resource.Error -> toValue = "Error"
            Resource.Loading -> toValue = "No value"
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(modifier = Modifier.padding(horizontal = 20.dp),
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = Color(0xff4CBB17),
                        contentDescription = "Menu",
                        modifier = Modifier.size(24.dp)
                    )
                },
                actions = {
                    Text(
                        text = "Sign up",
                        style = TextStyle(color = Color(0xff4CBB17), fontSize = 12.sp)
                    )
                })
        }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                Spacer(Modifier.height(32.dp))
            }
            item {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Currency Converter")
                        }

                        withStyle(style = SpanStyle(color = Color(color = 0xff4CBB17))) {
                            append(".")
                        }
                    }, style = TextStyle(
                        color = Color(0xff007FFF),
                        fontWeight = FontWeight.W700,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Start
                    ), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            // ====== First TextField (User Input) =======
            item {
                TextField(
                    value = fromValue,
                    onValueChange = { fromValue = it },
                    maxLines = 1,
                    placeholder = { Text("Enter Amount") },
                    trailingIcon = {
                        Text(selectedParentCountry?.countryCode ?: "")
                    },
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                Spacer(Modifier.height(8.dp))
            }
            // ====== Second TextField (Converted Result) =======
            item {
                // You might make this readOnly if you want the user
                // to see the result without editing it
                TextField(
                    value = toValue,
                    onValueChange = { /* no-op or if you want them to edit, handle here */ },
                    readOnly = true,
                    maxLines = 1,
                    placeholder = { Text("Converted Amount") },
                    trailingIcon = {
                        Text(selectedSubCountry?.countryCode ?: "")
                    },
                    colors = androidx.compose.material3.TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
            }

            // ====== Currency Selectors =======
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SelectCountry(selectedParentCountry) {
                        showParentCountry = true
                    }
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.noun_exchange_5691759),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    SelectCountry(selectedSubCountry) {
                        showSubCountry = true
                    }
                }
            }

            // ====== Convert Button =======
            item {
                Button(
                    onClick = { // 6) Attempt a conversion
                        val amountDouble = fromValue.toDoubleOrNull() ?: 0.0
                        val fromId = selectedParentCountry!!.currencyId ?: ""
                        val toId = selectedSubCountry!!.currencyId ?: ""
                        fixerViewModel.loadLatestRates(
                            base = fromId, symbols = toId)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = true,
                    contentPadding = PaddingValues(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(color = 0xff4CBB17))
                ) {

                    Text(text = "Convert")
                }
            }
            item {
                Spacer(Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMidMonthRate = true
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Mid Market Exchange Rate at 13:38 UTC",
                        color = Color(0xff007FFF),
                        modifier = Modifier.drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val verticalOffset = size.height - 2.sp.toPx()
                            drawLine(
                                color = Color(0xff007FFF),
                                strokeWidth = strokeWidthPx,
                                start = Offset(0f, verticalOffset),
                                end = Offset(size.width, verticalOffset)
                            )
                        })
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.noun_info_7603665),
                        contentDescription = "z",
                        tint = ColorProducer { Color(0xff007FFF) }.invoke(),
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }

    // ====== Parent Country Sheet =======
    if (showParentCountry) {
        ModalBottomSheet(onDismissRequest = { showParentCountry = false }) {
            CurrenciesListContent(countries = countries,
                selectedCountry = selectedParentCountry ?: Country(
                    currencyId = "EUR",
                    currencyName = "Euro",
                    countryCode = "EU",
                    currencySymbol = "€",
                    flagUrl = "https://flagcdn.com/eu.svg"
                ),
                onItemSelected = { item ->
                    selectedParentCountry = item
                    showParentCountry = false
                })
        }
    }
    // ====== Sub Country Sheet =======
    if (showSubCountry) {
        ModalBottomSheet(onDismissRequest = { showSubCountry = false }) {
            CurrenciesListContent(countries = countries,
                selectedCountry = selectedSubCountry ?: Country(
                    currencyId = "EUR",
                    currencyName = "Euro",
                    countryCode = "EU",
                    currencySymbol = "€",
                    flagUrl = "https://flagcdn.com/eu.svg"
                ),
                onItemSelected = { item ->
                    selectedSubCountry = item
                    showSubCountry = false
                })
        }
    }
    if (showMidMonthRate) {
        ModalBottomSheet(
            containerColor = Color(0xff007FFF),
            onDismissRequest = { showMidMonthRate = false }) {
            MidMonthContent()
        }
    }

}

@Composable
private fun SelectCountry(
    selectedParentCountry: Country?, onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(48.dp)
            .border(
                BorderStroke(width = 2.dp, Color.LightGray), shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onClick() }) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current).data(selectedParentCountry?.flagUrl)
                .decoderFactory(SvgDecoder.Factory()).build()
        )
        val state by painter.state.collectAsState()
        when (state) {
            is AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }

            is AsyncImagePainter.State.Success -> {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    painter = painter,
                    contentDescription = stringResource(R.string.app_name),
                )
            }

            is AsyncImagePainter.State.Error -> {
                // Show some error UI.
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(selectedParentCountry?.countryCode ?: "")
        Spacer(Modifier.width(8.dp))
        Image(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CurrenciesListContent(
    countries: List<Country>, selectedCountry: Country, onItemSelected: (Country) -> Unit
) {
    SelectionTitleListOnlyContent(
        lazyScopeItems = {
            itemsIndexed(countries) { _, selectedCountryFromList ->
                SelectableItemCard(
                    modifier = Modifier,
                    country = selectedCountryFromList,
                    onSelected = {
                        onItemSelected(selectedCountryFromList)
                    },
                    selected = (selectedCountry.currencySymbol == selectedCountryFromList.currencySymbol)
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }, text = "Select Currency"
    )
}


@Composable
fun MidMonthContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val pages = listOf("Past 30 Days", "Past 90 Days")
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { pages.size })

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pages.forEachIndexed { index, title ->
                val isSelected = (pagerState.currentPage == index)
                Column(
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    if (isSelected) {
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color.Green, CircleShape)
                        )
                    }
                }
            }
        }
        HorizontalPager(
            state = pagerState,
        ) { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    LineSample8()
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Get Rate alert straight to your mailbox",
                    color = Color.White,
                    modifier = Modifier.drawBehind {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height - 2.sp.toPx()
                        drawLine(
                            color = Color.White,
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    }
                )
            }
        }
    }
}

