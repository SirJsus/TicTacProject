package com.jsus.tictacproject.ui.activities.task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.db.DBHelper
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.databinding.ItemToggleTimerBinding
import java.time.LocalDateTime

class ActivityTaskAdapter(
    private var items: List<Activity>,
    private val db: DBHelper,
    private val next: Boolean,
    private val onSelectionChange: (Int) -> Unit
): RecyclerView.Adapter<ActivityTaskAdapter.ActivityTaskModel>() {

    private var activeTimerPosition: Int? = null
    private val toggleStates = items.toMutableList()
    private var now: LocalDateTime = LocalDateTime.now()

    inner class ActivityTaskModel(view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemToggleTimerBinding.bind(view)
        val toggleButton: ToggleButton = binding.toggleButtonItem

        fun render (activity: Activity, position: Int){
            with(binding){
                Log.d("tictac_ActivityTaskAdapter", "render, ==========================")
                Log.d("tictac_ActivityTaskAdapter", "render, activity: $activity")
                toggleButtonItem.isChecked = if (activity.timer.isRunning){
                                                    activeTimerPosition = position
                                                    true
                                                } else false

                toggleButtonItem.text = activity.name
                toggleButtonItem.textOn = activity.name
                toggleButtonItem.textOff = activity.name

                Log.d("tictac_ActivityTaskAdapter", "render, isChecked: ${toggleButtonItem.isChecked}")
                Log.d("tictac_ActivityTaskAdapter", "render, prevPosition: $activeTimerPosition")
                Log.d("tictac_ActivityTaskAdapter", "render, newPosition: $position")

                toggleButtonItem.setOnCheckedChangeListener { _, isChecked ->
                    Log.d("tictac_ActivityTaskAdapter", "render, reset ============================")
                    Log.d("tictac_ActivityTaskAdapter", "render, isChecked change: $isChecked")
                    Log.d("tictac_ActivityTaskAdapter", "render, prevPosition: $activeTimerPosition")
                    Log.d("tictac_ActivityTaskAdapter", "render, newPosition: $position")

                    now = LocalDateTime.now()
                    if (isChecked) {
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
                        onSelectionChange(position)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTaskModel {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toggle_timer, parent, false)
        return ActivityTaskModel(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ActivityTaskModel, position: Int) {
        holder.toggleButton.setOnCheckedChangeListener(null)
        holder.toggleButton.isChecked = position == activeTimerPosition
        holder.toggleButton.text = items[position].name
        holder.render(items[position], position)
    }

    // Función para cambiar el estado de un toggle en una posición
    fun setToggleState(newPosition: Int) {
        now = LocalDateTime.now()
        if (activeTimerPosition != null) {
            Activity().stopTimer(toggleStates[activeTimerPosition!!], now, db)
            notifyItemChanged(activeTimerPosition!!)
        } else {
            val getNow = db.getNowActivity()
            if (getNow != Activity()) Activity().stopTimer(getNow, now, db)
        }
        Activity().startTime(toggleStates[newPosition], now, db)
        notifyItemChanged(newPosition)
    }
}