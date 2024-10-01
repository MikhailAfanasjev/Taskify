package com.example.taskify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskify.R
import com.example.taskify.Task
import com.example.taskify.adapter.TaskAdapter
import com.example.taskify.TaskViewModel
import com.example.taskify.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)

        // Настройка RecyclerView
        adapter = TaskAdapter()
        binding.recyclerViewTasks.adapter = adapter
        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())

        taskViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
        }

        // Переход на экран добавления задачи
        binding.buttonAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
        }
        adapter.onClickDelete { task: Task ->
            taskViewModel.deleteTask(task)
        }

        return binding.root
    }
}