package com.example.taskify.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.taskify.Task
import com.example.taskify.db.TaskDao
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    fun getTasks(): LiveData<List<Task>> {
        val flow = taskDao.getTasks()
        return flow?.map { tasks -> tasks ?: emptyList() }?.asLiveData() ?: MutableLiveData(emptyList())
    }
}