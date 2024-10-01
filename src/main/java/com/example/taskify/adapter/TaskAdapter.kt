package com.example.taskify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskify.Task
import com.example.taskify.databinding.ItemTaskBinding

class TaskAdapter : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {
    private var onClickDeleteListener: ((Task) -> Unit)? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class TaskViewHolder(private val binding: ItemTaskBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(task: Task) {
                    binding.taskTitle.text =task.name
                    binding.taskDateTime.text = "${task.date} ${task.time}"
                    binding.deleteButton.setOnClickListener{
                        onClickDeleteListener?.invoke(task)
                    }
                }
            }
    fun onClickDelete(listener: (Task) -> Unit) {
        onClickDeleteListener = listener
    }
    class TaskDiffCallback: DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}