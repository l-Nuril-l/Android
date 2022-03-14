package com.example.taskintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.taskintent.database.TaskDatabase
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.coroutineContext

private const val DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context) {


    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTasks(): LiveData<List<Task>> = taskDao.getTasks()
    fun getTask(id: UUID): LiveData<Task?> = taskDao.getTask(id)
    fun addTask(task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        executor.execute {
            taskDao.updateTask(task)
        }
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
//
//                INSTANCE!!.addTask(Task(title="Task 1"))
//                INSTANCE!!.addTask(Task(title="Task 2"))
//                INSTANCE!!.addTask(Task(title="Task 3"))
//                INSTANCE!!.addTask(Task(title="Task 4"))
//                INSTANCE!!.addTask(Task(title="Task 5"))
            }
        }

        fun get(): TaskRepository {
            return INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }

    }

}