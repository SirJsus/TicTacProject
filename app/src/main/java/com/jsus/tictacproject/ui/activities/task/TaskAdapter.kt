package com.jsus.tictacproject.ui.activities.task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.databinding.ItemTaskBinding
import java.time.LocalDateTime
import kotlin.coroutines.coroutineContext

class TaskAdapter(
    private val items: List<Task>,
    dbHelper: DBHelper
): RecyclerView.Adapter<TaskAdapter.TaskViewModel>() {
    val db = dbHelper
    private var currentPosition = -1 // Posición actual para activar el toggle

    inner class TaskViewModel(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemTaskBinding.bind(view)
        fun render(task: Task){
            //
            with(binding){
                textName.text = task.name
                textDesc.text = task.desc

                timerTaskRv.layoutManager = LinearLayoutManager(binding.root.context)
                val adapter = ActivityTaskAdapter(task.listActivity, db, false){ newPosition ->
                    currentPosition = newPosition
                    Log.d("tictac_TaskAdapter", "playButton, ActivityTaskAdapter: $newPosition")
                }
                timerTaskRv.adapter = adapter

                playButton.setOnClickListener {
                    Log.d("tictac_TaskAdapter", "playButton, click ----------------------")
                    Log.d("tictac_TaskAdapter", "playButton, currentPosition: $currentPosition")
                    Log.d("tictac_TaskAdapter", "playButton, adapter.itemCount: ${adapter.itemCount - 1}")

                    if (currentPosition < adapter.itemCount - 1) {
                        currentPosition++
                    } else {
                        currentPosition = 0
                    }
                    adapter.setToggleState(currentPosition)
                }
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
        holder.render(items[position])
    }
}