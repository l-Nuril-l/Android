package com.example.taskintent

import androidx.lifecycle.ViewModel
import java.util.*

class TaskListViewModel : ViewModel() {
    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()


}