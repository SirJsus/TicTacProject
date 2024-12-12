package com.jsus.tictacproject.ui.activities.activities

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.Register
import com.jsus.tictacproject.databinding.ItemToggleTimerBinding
import java.time.LocalDateTime

class TimerAdapter(
    private val items: List<Activity>,
    private val db: DBHelper
): RecyclerView.Adapter<TimerAdapter.TimerViewHolder>()  {

    private var activeTimerPosition: Int? = null
    private var now: LocalDateTime = LocalDateTime.now()

    inner class TimerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemToggleTimerBinding.bind(view)
        val toggleButton: ToggleButton = binding.toggleButtonItem

        fun render (activity: Activity, position: Int){
            with(binding){
                Log.d("tictac_TimerAdapter", "render, activity: $activity")
                toggleButtonItem.isChecked = if (activity.timer.isRunning){
                    activeTimerPosition = position
                    true
                } else false
                toggleButtonItem.text = activity.name
                toggleButtonItem.textOn = activity.name
                toggleButtonItem.textOff = activity.name

                toggleButtonItem.setOnCheckedChangeListener { _, isChecked ->
                    Log.d("tictac_TimerAdapter", "render, reset ============================")
                    Log.d("tictac_TimerAdapter", "render, isChecked: $isChecked")
                    Log.d("tictac_TimerAdapter", "render, prevPosition: $activeTimerPosition")
                    Log.d("tictac_TimerAdapter", "render, newPosition: $position")

                    if (isChecked) {
                        val now = LocalDateTime.now()
                        // Detener el cronómetro activo, si hay uno
                        activeTimerPosition?.let { prevPosition ->
                            if (prevPosition != position) {
                                val prevTimerItem = items[prevPosition]
                                activity.stopTimer(prevTimerItem, now, db)
                                notifyItemChanged(prevPosition) // Actualizar la UI del cronómetro anterior
                            }
                        }
                        // Iniciar el nuevo cronómetro
                        activeTimerPosition = position
                        Log.d("tictac_TimerAdapter", "render, new activeTimerPosition: $activeTimerPosition")
                        activity.startTime(activity, now, db)
                    } else {
                        // Detener el cronómetro actual
                        activity.stopTimer(activity, now, db)
                        if (activeTimerPosition == position) activeTimerPosition = null
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toggle_timer, parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        holder.toggleButton.setOnCheckedChangeListener(null)
        holder.toggleButton.isChecked = position == activeTimerPosition
        holder.toggleButton.text = items[position].name


        holder.render(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    private fun startTimer(activity: Activity) {
        now = LocalDateTime.now()
        activity.timer.start(now)
        Log.d("tictac_TimerAdapter", "startTimer, activity: $activity")
        db.insertNow(activity)
    }

    private fun stopTimer(activity: Activity) {
        if (activity.timer.isRunning){
            val now = LocalDateTime.now()
            activity.timer.end(now)
            val newReg = Register().create(activity, db)
            db.deleteNow(1, newReg.activity)
            Log.d("tictac_TimerAdapter", "stopTimer, activity: $activity")
            activity.timer.reset()
        }
    }

}