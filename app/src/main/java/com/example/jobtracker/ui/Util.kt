package com.example.jobtracker.ui

import androidx.compose.ui.graphics.Color
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.data.Status
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

val IdLocale: Locale = Locale("id", "ID")

val Status.color: Color
    get() = when (this) {
        Status.DIKIRIM -> Color(0xFF2196F3)
        Status.WAWANCARA -> Color(0xFFFFA000)
        Status.PSIKOTES -> Color(0xFF65386F)
        Status.DITERIMA -> Color(0xFF43A047)
        Status.DITOLAK -> Color(0xFFE53935)
    }

fun formatDate(epochDay: Long): String =
    LocalDate.ofEpochDay(epochDay).format(DateTimeFormatter.ofPattern("d MMM yyyy", IdLocale))

fun daysAgoText(epochDay: Long): String {
    val d = ChronoUnit.DAYS.between(LocalDate.ofEpochDay(epochDay), LocalDate.now())
    return when {
        d == 0L -> "Hari ini"
        d == 1L -> "Kemarin"
        d > 1 -> "$d hari yang lalu"
        else -> "${-d} hari lagi"
    }
}

fun normalizeLink(raw: String): String {
    val t = raw.trim()
    return if (t.isEmpty() || t.startsWith("http://") || t.startsWith("https://")) t else "https://$t"
}

data class DashboardStats(
    val total: Int,
    val byStatus: Map<Status, Int>,
    val monthly: List<Pair<String, Int>>,
    /** Persentase lamaran yang sudah sampai tahap wawancara atau diterima. */
    val progressRate: Int
)

fun computeStats(list: List<JobApplication>): DashboardStats {
    val byStatus = Status.entries.associateWith { s -> list.count { it.status == s } }
    val now = YearMonth.now()
    val monthly = (5 downTo 0).map { back ->
        val ym = now.minusMonths(back.toLong())
        val label = ym.month.getDisplayName(TextStyle.SHORT, IdLocale)
        label to list.count { YearMonth.from(LocalDate.ofEpochDay(it.applyDate)) == ym }
    }
    val advanced = (byStatus[Status.WAWANCARA] ?: 0) + (byStatus[Status.DITERIMA] ?: 0)
    val rate = if (list.isEmpty()) 0 else advanced * 100 / list.size
    return DashboardStats(list.size, byStatus, monthly, rate)
}
