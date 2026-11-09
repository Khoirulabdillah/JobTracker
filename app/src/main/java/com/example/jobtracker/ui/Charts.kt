package com.example.jobtracker.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Diagram donat: setiap irisan = (warna, jumlah). */
@Composable
fun DonutChart(
    slices: List<Pair<Color, Int>>,
    centerTitle: String,
    centerSubtitle: String,
    modifier: Modifier = Modifier
) {
    val total = slices.sumOf { it.second }
    val track = MaterialTheme.colorScheme.surfaceVariant
    val progress = remember { Animatable(0f) }
    LaunchedEffect(slices) {
        progress.snapTo(0f)
        progress.animateTo(1f, tween(900))
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val strokeWidth = 26.dp.toPx()
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            drawArc(
                color = track, startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = topLeft, size = arcSize, style = Stroke(strokeWidth)
            )
            if (total > 0) {
                val visible = slices.count { it.second > 0 }
                val gap = if (visible > 1) 3f else 0f
                var start = -90f
                slices.forEach { (color, value) ->
                    if (value > 0) {
                        val sweep = 360f * value / total
                        drawArc(
                            color = color,
                            startAngle = start + gap / 2,
                            sweepAngle = (sweep - gap) * progress.value,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(strokeWidth)
                        )
                        start += sweep
                    }
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(centerTitle, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                centerSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Diagram batang sederhana: setiap item = (label, jumlah). */
@Composable
fun BarChart(
    data: List<Pair<String, Int>>,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    val max = (data.maxOfOrNull { it.second } ?: 0).coerceAtLeast(1)
    val empty = MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier.fillMaxWidth().height(150.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (label, value) ->
            val fraction by animateFloatAsState(value / max.toFloat(), tween(700), label = "bar")
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(value.toString(), style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(4.dp))
                Box(
                    Modifier
                        .width(26.dp)
                        .height((90 * fraction).dp.coerceAtLeast(4.dp))
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(if (value > 0) barColor else empty)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
