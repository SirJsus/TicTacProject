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

class TimerToTaskAdapter(
    private val items: List<Activity>,
    private val db: DBHelper,
    private val selectedList: List<Activity>,
    private val activitySelected: (Activity, add: Boolean) -> Unit
): RecyclerView.Adapter<TimerToTaskAdapter.TimerToTaskViewHolder>() {

    inner class TimerToTaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemToggleTimerBinding.bind(view)
        val toggleButton: ToggleButton = binding.toggleButtonItem

        fun render (activity: Activity, position: Int){
            with(binding){
                Log.d("tictac_TimerToTaskAdapter", "render, activity: $activity")
                val index = selectedList.indexOf(activity)
                if (index != -1){
                    toggleButton.isChecked = true
                    toggleButtonItem.text = "${index+1}) ${activity.name}"
                } else {
                    toggleButton.isChecked = false
                    toggleButtonItem.text = activity.name
                }

                toggleButtonItem.setOnCheckedChangeListener { _, isChecked ->
                    activitySelected(activity, isChecked)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerToTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toggle_timer, parent, false)
        return TimerToTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerToTaskViewHolder, position: Int) {
        holder.toggleButton.setOnCheckedChangeListener(null)
        holder.toggleButton.text = items[position].name
        holder.render(items[position], position)
    }

    override fun getItemCount(): Int = items.size

}