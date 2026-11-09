package com.example.jobtracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobtracker.data.AppDatabase
import com.example.jobtracker.data.JobApplication
import com.example.jobtracker.data.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JobViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).jobDao()

    val all: StateFlow<List<JobApplication>> =
        dao.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val query = MutableStateFlow("")
    val filter = MutableStateFlow<Status?>(null)

    val filtered: StateFlow<List<JobApplication>> =
        combine(all, query, filter) { list, q, f ->
            val keyword = q.trim()
            list.filter { job ->
                (f == null || job.status == f) &&
                    (keyword.isEmpty() ||
                        job.company.contains(keyword, ignoreCase = true) ||
                        job.position.contains(keyword, ignoreCase = true))
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun observe(id: Long) = dao.observeById(id)

    suspend fun get(id: Long): JobApplication? = dao.getById(id)

    fun save(job: JobApplication) = viewModelScope.launch {
        if (job.id == 0L) dao.insert(job) else dao.update(job)
    }

    fun restore(job: JobApplication) = viewModelScope.launch { dao.insert(job) }

    fun delete(job: JobApplication) = viewModelScope.launch { dao.delete(job) }
}
