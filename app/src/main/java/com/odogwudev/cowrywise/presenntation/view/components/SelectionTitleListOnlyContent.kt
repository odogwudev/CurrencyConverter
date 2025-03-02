package com.odogwudev.cowrywise.presenntation.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectionTitleListOnlyContent(
    modifier: Modifier = Modifier, lazyScopeItems: LazyListScope.() -> Unit, text: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(11.dp))
            Divider(thickness = 0.5.dp, color = Color(0XFFE4DFDF))
            Spacer(modifier = Modifier.height(11.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    lazyScopeItems()
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

            }
        }
    }
}