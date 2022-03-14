package com.example.taskintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.taskintent.Task
import java.util.*

@Dao
interface TaskDao {

    @Query("select * from task")
    fun getTasks(): LiveData<List<Task>>
    @Query("select * from task where id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Update
    fun updateTask(task: Task)
    @Insert
    fun addTask(task: Task)
}