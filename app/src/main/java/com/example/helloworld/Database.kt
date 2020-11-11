package com.example.helloworld

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
    val onComplete: Boolean
)

fun Long.toDateText(): String {
    return SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE).format(Date(this))
}

@Dao
interface TaskDao {
    @Query("Select * From task")
    suspend fun getAll(): List<Task>

    @Query("Select * from task where id = :id")
    suspend fun getTask(id: Int): Task

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}