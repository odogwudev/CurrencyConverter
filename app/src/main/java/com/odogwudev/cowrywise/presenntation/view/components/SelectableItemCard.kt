package com.odogwudev.cowrywise.presenntation.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.odogwudev.cowrywise.R
import com.odogwudev.cowrywise.data.model.Country

@Composable
fun SelectableItemCard(
    modifier: Modifier,
    country: Country,
    color: Color = Color.Black,
    onSelected: () -> Unit,
    cornerRadius: Dp = 0.dp,
    selected: Boolean,
) {
    Card(
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable {
                onSelected()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color.Gray else Color.LightGray
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 14.65.dp, end = 15.15.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(country.flagUrl)
                        .decoderFactory(SvgDecoder.Factory())
                        .build()
                )
                val state by painter.state.collectAsState()
                when (state) {
                    is AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is AsyncImagePainter.State.Success -> {
                        Image(
                            modifier = Modifier.size(30.dp)
                                .clip(CircleShape),
                            painter = painter,
                            contentDescription = stringResource(R.string.app_name),
                        )
                    }

                    is AsyncImagePainter.State.Error -> {
                        // Show some error UI.
                    }
                }

                Text(
                    text = country.currencyName, style = TextStyle(
                        color = color, fontSize = 17.sp
                    ), modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }
    }
}