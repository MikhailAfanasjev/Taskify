package com.example.taskify.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.taskify.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskRememberWorker
@Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Извлекаем имя задачи из переданных данных
        val taskName = inputData.getString("task_name") ?: return Result.failure()

        // Отправка уведомления пользователю
        return try {
            if (checkNotificationPermission()) {
                showNotification(taskName)
                Result.success()
            } else {
                // Логгируем, что уведомление не было показано из-за отсутствия разрешений
                Result.failure()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun checkNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Для Android 13 (API 33) и выше, нужно проверить разрешение на уведомления
            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            // Для версий ниже Android 13 разрешение не требуется
            true
        }
    }

    private fun showNotification(taskName: String) {
        // Создаем URI для пользовательского звука из папки res/raw
        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/${R.raw.custom_sound}")

        // Настройка AudioAttributes для управления поведением звука
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION) // Используется для уведомлений
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) // Звук уведомлений
            .build()

        // Создание канала для уведомлений (для API >= 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_reminder_channel",
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for task reminders"
                setSound(soundUri, audioAttributes) // Устанавливаем звук
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Создание и отправка уведомления
        val notification = NotificationCompat.Builder(context, "task_reminder_channel")
            .setSmallIcon(R.drawable.ic_notification) // Иконка уведомления
            .setContentTitle("Напоминание о задаче")
            .setContentText("Не забудьте: $taskName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri) // Устанавливаем звук для уведомления
            .setAutoCancel(true)
            .build()

        // Проверяем разрешение перед отправкой уведомления
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            NotificationManagerCompat.from(context).notify(1, notification)
        }
    }
}