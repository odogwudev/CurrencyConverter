package com.odogwudev.cowrywise.presenntation.view

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.*

val gridProperties = GridProperties(
    xAxisProperties = GridProperties.AxisProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f,15f), phase = 10f),
    ),
    yAxisProperties = GridProperties.AxisProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f,15f), phase = 10f),
    ),
)
val dividerProperties = DividerProperties(
    xAxisProperties = LineProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f,15f), phase = 10f),
    ),
    yAxisProperties = LineProperties(
        thickness = .2.dp,
        color = SolidColor(Color.Gray.copy(alpha = .5f)),
        style = StrokeStyle.Dashed(intervals = floatArrayOf(15f,15f), phase = 10f),
    )
)
@Composable
fun RowScope.LineSample8() {
    val data = remember {
        listOf(
            Line(
                label = "",
                values = listOf(
                    10.0,
                    20.0,
                    7.0,
                    35.0,
                    20.0
                ),
                color = SolidColor(Color(0xff5A47CF)),
                firstGradientFillColor = Color(0xff6655CF).copy(alpha = .5f),
                secondGradientFillColor = Color.Transparent,
                strokeAnimationSpec = tween(2000, easing = EaseInOutCubic),
                gradientAnimationDelay = 1000,
                drawStyle = DrawStyle.Stroke(3.dp)
            ),
        )
    }
    Card(modifier=Modifier.height(270.dp).fillMaxWidth().weight(1f).padding(horizontal = 22.dp)
        .border(2.dp,Color.Transparent, RoundedCornerShape(12.dp)),
        elevation = CardDefaults.elevatedCardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xff007FFF)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(vertical = 12.dp)){
            LineChart(
                indicatorProperties = HorizontalIndicatorProperties(
                    textStyle = TextStyle(
                        fontSize = 11.sp, color = Color.White
                    ),
                    contentBuilder = {
                        "."
                    }
                ),
                modifier = Modifier
                    .fillMaxSize(),
                data = data,
                animationMode = AnimationMode.Together(delayBuilder = {
                    it * 500L
                }),
                dividerProperties = DividerProperties(enabled = false),
                gridProperties = GridProperties(enabled = false),
                popupProperties = PopupProperties(
                    textStyle = TextStyle(
                        fontSize = 11.sp,
                        color = Color.White,
                        
                    ),
                    contentBuilder = {
                        it.format(1) + " Million"
                    },
                    containerColor = Color.Green
                ),
                labelHelperProperties = LabelHelperProperties(textStyle = TextStyle(fontSize = 12.sp, color = Color.White)),
                curvedEdges = true,
                labelProperties = LabelProperties(
                    enabled = true,
                    labels = listOf("01 Jun","07 Jun","15 Jun","23 Jun","30 Jun"),
                    textStyle = TextStyle(
                        fontSize = 11.sp, color = Color.White
                    ),
                ),
            )
        }
    }
}
