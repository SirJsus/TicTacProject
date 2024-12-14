package com.jsus.tictacproject.ui.activities.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.databinding.ItemToggleTimerBinding

class ActivityTaskAdapter(
    private var activityList: List<Activity>
): RecyclerView.Adapter<ActivityTaskAdapter.ActivityTaskModel>() {
    inner class ActivityTaskModel(view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemToggleTimerBinding.bind(view)
        fun render (activity: Activity){
            with(binding){
                toggleButtonItem.text = activity.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityTaskModel {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toggle_timer, parent, false)
        return ActivityTaskModel(view)
    }

    override fun getItemCount(): Int = activityList.size

    override fun onBindViewHolder(holder: ActivityTaskModel, position: Int) {
        holder.render(activityList[position])
    }
}