package com.jsus.tictacproject.ui.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Task
import com.jsus.tictacproject.code.objects.TextFormat
import com.jsus.tictacproject.databinding.ItemTimerOnBinding
import java.time.Duration
import java.time.LocalDateTime

class TimerOnAdapter(private val items: List<Activity>,
                     private val task: Task,
                     private val db: DBHelper,
                     private val listener: ActivityChange
): RecyclerView.Adapter<TimerOnAdapter.TimerOnHolder>() {
    private val handler = Handler(Looper.getMainLooper())

    inner class TimerOnHolder (view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemTimerOnBinding.bind(view)
        val timerButton = binding.timerOnButton

        fun render (activity: Activity){
            timerButton.setOnClickListener {
                val now = LocalDateTime.now()
                activity.stopTimer(activity, now, db)
                Task().stop(db)
                listener.activityHasChange()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerOnHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timer_on, parent, false)
        return TimerOnHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TimerOnHolder, position: Int) {
        val item = items[position]
        handler.post(object : Runnable {
            override fun run() {
                if (item.timer.isRunning) {
                    val between = Duration.between(item.timer.start, LocalDateTime.now()).toMillis()
                    holder.timerButton.text = if (Task() == task) "${item.name}\n${TextFormat.getTime(between)}"
                                                else "${task.name} - ${item.name}\n${TextFormat.getTime(between)}"
                    handler.postDelayed(this, 55)
                }
            }
        })
        holder.render(item)
    }
}