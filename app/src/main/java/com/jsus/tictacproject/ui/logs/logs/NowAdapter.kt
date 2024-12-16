package com.jsus.tictacproject.ui.logs.logs

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.objects.Activity
import com.jsus.tictacproject.code.objects.TextFormat
import com.jsus.tictacproject.databinding.ItemRegisterTimerBinding
import java.time.Duration
import java.time.LocalDateTime

class NowAdapter(private val items: List<Activity>
): RecyclerView.Adapter<NowAdapter.NowHolder>() {
    private val handler = Handler(Looper.getMainLooper())

    inner class NowHolder (view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemRegisterTimerBinding.bind(view)
        fun render (activity: Activity){
            with(binding){
                textName.text = activity.name
                textStart.text = TextFormat.getLocalTime(activity.timer.start!!)

                val dateStart = activity.timer.start!!.toLocalDate()
                val dateEnd = LocalDateTime.now().toLocalDate()
                textDate.text = if (dateEnd == dateStart) dateStart.toString()
                                else "$dateStart - $dateEnd"
            }
            startTimer(activity, binding)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_register_timer, parent, false)
        return  NowHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NowHolder, position: Int) {
        holder.render(items[position])
    }

    private fun startTimer(activity: Activity, binding: ItemRegisterTimerBinding) {
        handler.post(object : Runnable {
            override fun run() {
                if (activity.timer.isRunning) {
                    with(binding){
                        val duration = Duration.between(activity.timer.start, LocalDateTime.now()).toMillis()
                        textDuration.text = TextFormat.getTime(duration)
                        textEnd.text = TextFormat.getLocalTime(LocalDateTime.now())
                    }
                    handler.postDelayed(this, 55)
                }
            }
        })
    }
}