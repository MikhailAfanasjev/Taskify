package com.example.taskify.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room.databaseBuilder
import com.example.taskify.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: TaskDatabase? = null
        @Synchronized
        fun getInstance(context: Context): TaskDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java, "task_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}