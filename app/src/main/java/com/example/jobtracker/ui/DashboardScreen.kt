package com.example.jobtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.data.Status
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val Status.icon: ImageVector
    get() = when (this) {
        Status.DIKIRIM -> Icons.AutoMirrored.Filled.Send
        Status.WAWANCARA -> Icons.Default.Groups
        Status.PSIKOTES -> Icons.Default.Psychology
        Status.DITERIMA -> Icons.Default.CheckCircle
        Status.DITOLAK -> Icons.Default.Cancel
    }

@Composable
fun DashboardScreen(
    jobs: List<JobApplication>,
    onOpen: (Long) -> Unit,
    onSeeAll: () -> Unit,
    onStatusClick: (Status) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val stats = remember(jobs) { computeStats(jobs) }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "greeting") { Greeting() }
        item(key = "summary") { SummaryCard(stats) }

        if (jobs.isEmpty()) {
            item(key = "empty") { EmptyCard(onAdd) }
        } else {
            item(key = "stats") { StatGrid(stats, onStatusClick) }
            item(key = "chart") { StatusChartCard(stats) }
            item(key = "monthly") { MonthlyChartCard(stats) }
            item(key = "recent-header") {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Lamaran terbaru",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    )
                    TextButton(onClick = onSeeAll) { Text("Lihat semua") }
                }
            }
            items(jobs.take(3), key = { it.id }) { job ->
                JobCard(job, onClick = { onOpen(job.id) })
            }
        }
    }
}

@Composable
private fun Greeting() {
    val hour = remember { LocalTime.now().hour }
    val sapa = when {
        hour < 11 -> "Selamat pagi"
        hour < 15 -> "Selamat siang"
        hour < 18 -> "Selamat sore"
        else -> "Selamat malam"
    }
    val today = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", IdLocale))
    }
    Column(Modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
        Text(sapa, style = MaterialTheme.typography.headlineSmall)
        Text(
            today,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Kartu utama: total lamaran dan bilah progres lanjut-tahap (statis, tanpa animasi). */
@Composable
private fun SummaryCard(stats: DashboardStats) {
    val container = MaterialTheme.colorScheme.primaryContainer
    val onContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val primary = MaterialTheme.colorScheme.primary

    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(container, lerp(container, primary, 0.35f))))
    ) {
        // Lingkaran dekoratif lembut di pojok kanan atas
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .offset(x = 44.dp, y = (-44).dp)
                .size(150.dp)
                .clip(CircleShape)
                .background(primary.copy(alpha = 0.10f))
        )
        Column(Modifier.fillMaxWidth().padding(24.dp)) {
            Text(
                "Total lamaran",
                style = MaterialTheme.typography.titleSmall,
                color = onContainer.copy(alpha = 0.8f)
            )
            Text(
                "${stats.total}",
                style = MaterialTheme.typography.displayMedium,
                color = onContainer
            )
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    if (stats.total == 0) "Mulai kirim lamaran pertamamu"
                    else "Lanjut ke wawancara / diterima",
                    style = MaterialTheme.typography.bodyMedium,
                    color = onContainer,
                    modifier = Modifier.weight(1f)
                )
                if (stats.total > 0) {
                    Text(
                        "${stats.progressRate}%",
                        style = MaterialTheme.typography.titleMedium,
                        color = onContainer
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { stats.progressRate / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = primary,
                trackColor = onContainer.copy(alpha = 0.15f),
                strokeCap = StrokeCap.Round,
                gapSize = 0.dp,
                drawStopIndicator = {}
            )
        }
    }
}

@Composable
private fun StatGrid(stats: DashboardStats, onStatusClick: (Status) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Status.entries.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                pair.forEach { status ->
                    StatCard(
                        status = status,
                        count = stats.byStatus[status] ?: 0,
                        onClick = { onStatusClick(status) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Jumlah status ganjil: sisakan ruang agar kartu terakhir tidak melebar penuh
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

/** Kartu status yang bisa diketuk untuk langsung membuka daftar dengan filter status tersebut. */
@Composable
private fun StatCard(status: Status, count: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = status.color.copy(alpha = 0.12f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                Modifier.size(38.dp).clip(CircleShape).background(status.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(status.icon, contentDescription = null, tint = status.color, modifier = Modifier.size(20.dp))
            }
            Column {
                Text("$count", style = MaterialTheme.typography.headlineMedium)
                Text(
                    status.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatusChartCard(stats: DashboardStats) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Komposisi status", style = MaterialTheme.typography.titleMedium)
            Text(
                "Sebaran semua lamaranmu",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DonutChart(
                    slices = Status.entries.map { it.color to (stats.byStatus[it] ?: 0) },
                    centerTitle = "${stats.total}",
                    centerSubtitle = "lamaran",
                    modifier = Modifier.size(140.dp)
                )
                Spacer(Modifier.width(20.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Status.entries.forEach { s ->
                        val count = stats.byStatus[s] ?: 0
                        val pct = if (stats.total == 0) 0 else count * 100 / stats.total
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ColorDot(s.color)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                s.label,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "$count",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "  $pct%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthlyChartCard(stats: DashboardStats) {
    val thisMonth = stats.monthly.lastOrNull()?.second ?: 0
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("Aktivitas melamar", style = MaterialTheme.typography.titleMedium)
            Text(
                "$thisMonth lamaran bulan ini · 6 bulan terakhir",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            BarChart(stats.monthly, MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun EmptyCard(onAdd: () -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                Modifier.size(72.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Work,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(34.dp)
                )
            }
            Text("Belum ada lamaran", style = MaterialTheme.typography.titleMedium)
            Text(
                "Catat lamaran pertamamu dan pantau perkembangannya lewat diagram di sini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Button(onClick = onAdd, shape = RoundedCornerShape(16.dp)) {
                Text("Tambah lamaran pertama")
            }
        }
    }
}