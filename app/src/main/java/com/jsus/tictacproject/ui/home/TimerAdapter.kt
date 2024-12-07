package com.jsus.tictacproject.ui.home

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.objects.TimerItem
import com.jsus.tictacproject.databinding.ItemToggleTimerBinding
import java.time.LocalDateTime

class TimerAdapter(
    private val items: List<TimerItem>
): RecyclerView.Adapter<TimerAdapter.TimerViewHolder>()  {

    private val handler = Handler(Looper.getMainLooper())
    private var activeTimerPosition: Int? = null
    private var now: LocalDateTime = LocalDateTime.now()

    inner class TimerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemToggleTimerBinding.bind(view)

        val toggleButton: ToggleButton = binding.toggleButtonItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_toggle_timer, parent, false)
        return TimerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        val timerItem = items[position]

        // Actualizar el estado inicial
        holder.toggleButton.isChecked = timerItem.timer.isRunning
        holder.toggleButton.text = "${timerItem.name}\n${formatTime(timerItem.timer.getElapsedTime())}"

        // Configurar el botón toggle
        holder.toggleButton.setOnCheckedChangeListener { _, isChecked ->
            /*if (isChecked) {
                startTimer(holder, timerItem)
            } else {
                stopTimer(timerItem)
            }*/

            if (isChecked) {
                // Detener el cronómetro activo, si hay uno
                activeTimerPosition?.let { prevPosition ->
                    if (prevPosition != position) {
                        val prevTimerItem = items[prevPosition]
                        prevTimerItem.timer.reset()
                        notifyItemChanged(prevPosition) // Actualizar la UI del cronómetro anterior
                    }
                }

                // Iniciar el nuevo cronómetro
                activeTimerPosition = position
                startTimer(holder, timerItem)
            } else {
                // Detener el cronómetro actual
                stopTimer(timerItem)
                if (activeTimerPosition == position) activeTimerPosition = null
            }
        }
    }

    override fun getItemCount(): Int = items.size

    private fun startTimer(holder: TimerViewHolder, timerItem: TimerItem) {

        now = LocalDateTime.now()
        timerItem.timer.start(now)

        handler.post(object : Runnable {
            override fun run() {
                if (timerItem.timer.isRunning) {
                    holder.toggleButton.text = "${timerItem.name}\n${formatTime(timerItem.timer.getElapsedTime())}"
                    handler.postDelayed(this, 55)
                }
            }
        })
    }

    private fun stopTimer(timerItem: TimerItem) {
        timerItem.timer.reset()
    }

    private fun formatTime(milliseconds: Long): String {
        val mill = (milliseconds / 10) % 100
        val seconds = (milliseconds / 1000) % 60
        val minutes = (milliseconds / (1000 * 60)) % 60
        val hours = (milliseconds / (1000 * 60 * 60))
        return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, mill)
    }

}