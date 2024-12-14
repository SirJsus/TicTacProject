package com.jsus.tictacproject.ui.activities.task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.databinding.ItemTaskBinding

class TaskAdapter(
    private val items: List<Task>
): RecyclerView.Adapter<TaskAdapter.TaskViewModel>() {
    inner class TaskViewModel(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemTaskBinding.bind(view)
        val timerRv = binding.timerTaskRv
        fun render(task: Task){
            //
            with(binding){
                textId.text = task.id.toString()
                textName.text = task.name
                textDesc.text = task.desc
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewModel {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewModel(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewModel, position: Int) {
        val item = items[position]
        holder.binding.timerTaskRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ActivityTaskAdapter(item.listActivity)
        }
        holder.render(item)
    }
}