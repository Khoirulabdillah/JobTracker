package com.example.jobtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM applications ORDER BY applyDate DESC, id DESC")
    fun observeAll(): Flow<List<JobApplication>>

    @Query("SELECT * FROM applications WHERE id = :id")
    fun observeById(id: Long): Flow<JobApplication?>

    @Query("SELECT * FROM applications WHERE id = :id")
    suspend fun getById(id: Long): JobApplication?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobApplication): Long

    @Update
    suspend fun update(job: JobApplication)

    @Delete
    suspend fun delete(job: JobApplication)
}
