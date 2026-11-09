package com.example.jobtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Status(val label: String) {
    DIKIRIM("Dikirim"),
    WAWANCARA("Wawancara"),
    PSIKOTES("Psikotes"),
    DITERIMA("Diterima"),
    DITOLAK("Ditolak")
}

@Entity(tableName = "applications")
data class JobApplication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val company: String,
    val position: String,
    val link: String = "",
    /** Tanggal apply disimpan sebagai epoch day (LocalDate.toEpochDay). */
    val applyDate: Long,
    val status: Status = Status.DIKIRIM,
    val notes: String = ""
)
