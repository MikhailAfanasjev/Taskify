package com.example.taskify.di

import android.content.Context
import androidx.work.WorkerParameters
import com.example.taskify.worker.TaskRememberWorker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module // Модуль hilt для предоставления зависимостей
@InstallIn(SingletonComponent::class) // Указывает что зависимости будут жить в Singletonе
object AppModule {
    @Provides // Обозначает метод для добавления зависимостей
    fun provideTaskRememberWorker(context: Context, // Контекс приложения
                                  workerParams: WorkerParameters //Параметры для рабочего задания
    ): TaskRememberWorker {
      return TaskRememberWorker(context, workerParams)//Возвращаем экземпляр TaskRememberWorker
    }
}