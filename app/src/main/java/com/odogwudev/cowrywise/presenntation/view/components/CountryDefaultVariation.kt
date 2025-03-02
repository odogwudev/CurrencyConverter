package com.odogwudev.cowrywise.presenntation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odogwudev.cowrywise.R
import com.odogwudev.cowrywise.data.model.Country
import java.util.Locale

@Composable
fun CountryDefaultVariation(modifier: Modifier, placeholder: String, onClick: () -> Unit) {
    Row(
        modifier =modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = placeholder, style = TextStyle(
                color = VeryLightGray,
                fontSize = 24.sp,
            )
        )
        Spacer(Modifier.width(8.dp))
        Image(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "",
            modifier = Modifier.size(24.dp)
        )
    }
}


val TextColor = Color(0xFF333333)
val Ash = Color(0xFFE0E0E0)
val LightBlack = Color(0xFF464646)
fun String.toSentenceCase(): String {
    return this.lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
}

val VeryLightGray = Color(0xFFB1B1B1)