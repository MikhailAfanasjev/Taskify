package com.example.taskify.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.taskify.R
import com.example.taskify.Task
import com.example.taskify.TaskViewModel
import com.example.taskify.databinding.FragmentAddTaskBinding
import com.example.taskify.worker.TaskRememberWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private var selectedTime: String = ""
    private var selectedDate: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        binding.editTextTaskDate.setOnClickListener{
            showDatePickerDialog()
        }
        binding.editTextTaskTime.setOnClickListener{
            showTimePickerDialog()
        }
        // Обработчик нажатия на кнопку "Сохранить задачу"
        binding.buttonSaveTask.setOnClickListener {
            saveTask()
            }
        return binding.root
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickedDialog = DatePickerDialog(requireContext(), {_, selectedYear, selectedMonth,selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.editTextTaskDate.setText(selectedDate)
        },year, month, day)
        datePickedDialog.show()
    }
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickedDialog = TimePickerDialog(requireContext(), {_, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.editTextTaskTime.setText(selectedTime)
        }, hour, minute, true)
        timePickedDialog.show()
    }
    private fun saveTask() {
        val taskName = binding.editTextTaskName.text.toString().trim()

        if(taskName.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }
        val task = Task(name = taskName, time = selectedTime, date = selectedDate)
        taskViewModel.addTask(task)

        convertToTimestamps(task)
        Toast.makeText(requireContext(), "Задача сохранена", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
    }
    private  fun convertToTimestamps(task: Task) {
        val timeParts = task.time.split(":").map {it.toInt()}
        val dateParts = task.date.split("/").map {it.toInt()}
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, dateParts[2])
            set(Calendar.MONTH, dateParts[1]-1)
            set(Calendar.DAY_OF_MONTH, dateParts[0])
            set(Calendar.HOUR_OF_DAY, timeParts[0])
            set(Calendar.MINUTE, timeParts[1])
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val triggerTime = calendar.timeInMillis

        val data = workDataOf("task_name" to task.name)

        val workRequest = OneTimeWorkRequestBuilder<TaskRememberWorker>()
            .setInitialDelay(triggerTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }
}