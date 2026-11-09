@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.jobtracker.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.data.Status
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlinx.coroutines.delay

@Composable
fun FormScreen(
    initial: JobApplication?,
    onSave: (JobApplication) -> Unit,
    onBack: () -> Unit
) {
    var company by rememberSaveable { mutableStateOf(initial?.company ?: "") }
    var position by rememberSaveable { mutableStateOf(initial?.position ?: "") }
    var link by rememberSaveable { mutableStateOf(initial?.link ?: "") }
    var notes by rememberSaveable { mutableStateOf(initial?.notes ?: "") }
    var date by rememberSaveable { mutableStateOf(initial?.applyDate ?: LocalDate.now().toEpochDay()) }
    var status by rememberSaveable { mutableStateOf(initial?.status ?: Status.DIKIRIM) }
    var showPicker by rememberSaveable { mutableStateOf(false) }
    var tried by rememberSaveable { mutableStateOf(false) }

    val submit = {
        tried = true
        if (company.isNotBlank() && position.isNotBlank()) {
            val base = initial ?: JobApplication(company = "", position = "", applyDate = date)
            onSave(
                base.copy(
                    company = company.trim(),
                    position = position.trim(),
                    link = normalizeLink(link),
                    applyDate = date,
                    status = status,
                    notes = notes.trim()
                )
            )
        }
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(tonalElevation = 3.dp, shadowElevation = 8.dp) {
                Button(
                    onClick = submit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (initial == null) "Simpan lamaran" else "Simpan perubahan",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            FormHeader(
                title = if (initial == null) "Lamaran baru" else "Edit lamaran",
                subtitle = if (initial == null) "Catat lowongan yang kamu lamar"
                else initial.company,
                onBack = onBack
            )

            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Reveal(0) {
                    SectionCard("Informasi lowongan") {
                        FormField(
                            value = company,
                            onChange = { company = it },
                            label = "Nama perusahaan *",
                            icon = Icons.Default.Business,
                            error = tried && company.isBlank(),
                            errorText = "Wajib diisi"
                        )
                        FormField(
                            value = position,
                            onChange = { position = it },
                            label = "Posisi yang dilamar *",
                            icon = Icons.Default.Work,
                            error = tried && position.isBlank(),
                            errorText = "Wajib diisi"
                        )
                        FormField(
                            value = link,
                            onChange = { link = it },
                            label = "Link lowongan",
                            icon = Icons.Default.Link,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                        )
                    }
                }

                Reveal(1) {
                    SectionCard("Jadwal & status") {
                        Box {
                            OutlinedTextField(
                                value = formatDate(date),
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                label = { Text("Tanggal apply") },
                                leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                                supportingText = { Text(daysAgoText(date)) }
                            )
                            // Lapisan transparan agar seluruh field bisa diklik untuk membuka kalender
                            Box(
                                Modifier
                                    .matchParentSize()
                                    .clickable { showPicker = true }
                            )
                        }

                        Text(
                            "Status lamaran",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Status.entries.chunked(2).forEach { pair ->
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    pair.forEach { s ->
                                        StatusOption(
                                            status = s,
                                            selected = status == s,
                                            onClick = { status = s },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Reveal(2) {
                    SectionCard("Catatan") {
                        FormField(
                            value = notes,
                            onChange = { notes = it },
                            label = "Catatan (opsional)",
                            icon = Icons.AutoMirrored.Filled.Notes,
                            singleLine = false,
                            minLines = 3
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (showPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.ofEpochDay(date)
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let {
                        date = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate().toEpochDay()
                    }
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Batal") } }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

/** Header dengan gradasi lembut, tombol kembali, dan judul besar. */
@Composable
private fun FormHeader(title: String, subtitle: String, onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .statusBarsPadding()
            .padding(start = 12.dp, end = 20.dp, top = 8.dp, bottom = 20.dp)
    ) {
        FilledTonalIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
        }
        Spacer(Modifier.height(12.dp))
        Column(Modifier.padding(start = 8.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
            )
        }
    }
}

/** Muncul berurutan: fade + geser halus dari bawah. */
@Composable
private fun Reveal(index: Int, content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 90L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(450)) + slideInVertically(tween(450)) { it / 6 },
        exit = fadeOut()
    ) {
        content()
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
private fun FormField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    errorText: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        isError = error,
        supportingText = { if (error && errorText != null) Text(errorText) },
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = minLines
    )
}

/** Pilihan status berbentuk kartu; warna, garis tepi, dan skala beranimasi saat dipilih. */
@Composable
private fun StatusOption(
    status: Status,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background by animateColorAsState(
        if (selected) status.color.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surface,
        tween(250), label = "bg"
    )
    val borderColor by animateColorAsState(
        if (selected) status.color else MaterialTheme.colorScheme.outlineVariant,
        tween(250), label = "border"
    )
    val borderWidth by animateDpAsState(if (selected) 2.dp else 1.dp, tween(250), label = "width")
    val scale by animateFloatAsState(
        if (selected) 1.03f else 1f,
        spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    Surface(
        onClick = onClick,
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(16.dp),
        color = background,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Row(
            Modifier.padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ColorDot(status.color, 12)
            Spacer(Modifier.width(10.dp))
            Text(
                status.label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(selected, enter = fadeIn(tween(200)), exit = fadeOut(tween(150))) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = status.color,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}