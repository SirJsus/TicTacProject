package com.jsus.tictacproject.ui.activities.task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.databinding.ItemTaskBinding
import com.jsus.tictacproject.ui.home.ActivityChange
import java.time.LocalDateTime
import kotlin.coroutines.coroutineContext

class TaskAdapter(
    private val items: List<Task>,
    private val db: DBHelper,
    private val listener: ActivityChange
): RecyclerView.Adapter<TaskAdapter.TaskViewModel>(), ActivityChange {
    private var currentPosition = -1 // Posición actual para activar el toggle

    inner class TaskViewModel(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemTaskBinding.bind(view)
        fun render(task: Task, position: Int, adapter: ActivityTaskAdapter){
            //
            with(binding){
                textName.text = task.name
                textDesc.text = task.desc

                playButton.setOnClickListener {
                    if (position != 0) currentPosition = -1
                    Log.d("tictac_TaskAdapter", "playButton, click ----------------------")
                    Log.d("tictac_TaskAdapter", "playButton, currentPosition: $currentPosition")
                    Log.d("tictac_TaskAdapter", "playButton, adapter.itemCount: ${adapter.itemCount - 1}")

                    if (currentPosition < adapter.itemCount - 1) {
                        currentPosition++
                    } else {
                        currentPosition = 0
                    }
                    adapter.setToggleState(currentPosition)
                    updateThing()
                }

                playButton.setOnLongClickListener {
                    val now = LocalDateTime.now()
                    val getNow = Activity().getNow(db)
                    if (getNow != Activity()) Activity().stopTimer(getNow, now, db)
                    Task().stop(db)
                    Task().archived(task, db)
                    updateThing()
                    true
                }
            }
        }
    }

    fun updateThing(){
        listener.activityHasChange()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewModel {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewModel(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewModel, position: Int) {
        val item = items[position]

        val adapter = ActivityTaskAdapter(item, db, position, this){ newPosition ->
            currentPosition = newPosition
            Log.d("tictac_TaskAdapter", "playButton, ActivityTaskAdapter: $newPosition")
        }
        holder.binding.timerTaskRv.layoutManager = LinearLayoutManager(holder.binding.root.context)
        holder.binding.timerTaskRv.adapter = adapter


        if (item == Task()){
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
        } else holder.render(item, position, adapter)
    }

    override fun activityHasChange() {
        updateThing()
    }
}