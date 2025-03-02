package com.odogwudev.cowrywise.presenntation.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.odogwudev.cowrywise.data.model.Country

@Composable
fun SelectTextDropDown(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    description: String,
    country: Country,
    onClick: () -> Unit
) {
    Column(modifier = modifier) {
        if (description.isNotBlank()) {
            Text(
                text = description, style = TextStyle(
                    color = TextColor,
                    fontSize = 18.sp
                ), modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .height(48.dp)
                .border(BorderStroke(width = 2.dp, Color.LightGray), shape = RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = country.flagUrl,
                contentDescription = country.currencyName,
                modifier.size(width = 28.dp, height = 24.dp)
            )
            Spacer(Modifier.width(4.dp))
            if (isSelected) {
                RoleSelectedVariation(
                    modifier = Modifier, role = country.currencySymbol, isTitleCase = false
                ) { onClick() }
            } else {
                CountryDefaultVariation (
                    modifier = Modifier, placeholder = country.currencySymbol ?: "--Select--"
                ) { onClick() }
            }
        }
        Spacer(Modifier.height(5.dp))
    }
}
