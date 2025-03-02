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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleSelectedVariation(
    modifier: Modifier, isTitleCase: Boolean, role: String, onClick: () -> Unit
) {
    Row(
        modifier = modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isTitleCase) role.toSentenceCase() else role,
            style = TextStyle(
                color = TextColor,
                fontSize = 24.sp
            ),
            modifier = Modifier.weight(5f).padding(start = 15.dp, end = 15.dp),
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.width(8.dp))
        Image(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "",
            modifier = Modifier.size(24.dp)
        )
    }
}
