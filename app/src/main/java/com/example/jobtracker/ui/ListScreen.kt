package com.example.jobtracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.data.Status

@Composable
fun ListScreen(
    allJobs: List<JobApplication>,
    jobs: List<JobApplication>,
    query: String,
    onQueryChange: (String) -> Unit,
    filter: Status?,
    onFilterChange: (Status?) -> Unit,
    onOpen: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        // Judul halaman + ringkasan jumlah
        Column(Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 12.dp)) {
            Text("Daftar lamaran", style = MaterialTheme.typography.headlineSmall)
            Text(
                when {
                    allJobs.isEmpty() -> "Belum ada lamaran tercatat"
                    jobs.size == allJobs.size -> "${allJobs.size} lamaran tercatat"
                    else -> "Menampilkan ${jobs.size} dari ${allJobs.size} lamaran"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Kolom pencarian bentuk pil dengan latar lembut tanpa garis tepi
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(28.dp),
            placeholder = { Text("Cari perusahaan atau posisi") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Hapus pencarian")
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                unfocusedBorderColor = Color.Transparent
            )
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = filter == null,
                    onClick = { onFilterChange(null) },
                    label = { Text("Semua (${allJobs.size})") }
                )
            }
            items(Status.entries) { s ->
                FilterChip(
                    selected = filter == s,
                    onClick = { onFilterChange(if (filter == s) null else s) },
                    label = { Text("${s.label} (${allJobs.count { it.status == s }})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = s.color.copy(alpha = 0.2f),
                        selectedLabelColor = s.color
                    )
                )
            }
        }

        if (jobs.isEmpty()) {
            EmptyState(
                if (allJobs.isEmpty()) "Belum ada lamaran" else "Tidak ditemukan",
                if (allJobs.isEmpty()) "Tekan tombol + untuk mulai mencatat."
                else "Coba ubah kata kunci atau filter status."
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(jobs, key = { it.id }) { job ->
                    JobCard(job, onClick = { onOpen(job.id) })
                }
            }
        }
    }
}