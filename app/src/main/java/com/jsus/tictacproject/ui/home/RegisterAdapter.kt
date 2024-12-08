package com.jsus.tictacproject.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jsus.tictacproject.R
import com.jsus.tictacproject.code.objects.Register
import com.jsus.tictacproject.code.objects.TextFormat
import com.jsus.tictacproject.databinding.ItemRegisterTimerBinding

class RegisterAdapter(private val items: List<Register>
): RecyclerView.Adapter<RegisterAdapter.RegisterHolder>() {
    inner class RegisterHolder (view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemRegisterTimerBinding.bind(view)

        fun render (register: Register){
            with(binding){
                //Log.d("tictac_RegisterAdapter", "render, register: $register")
                textName.text = register.activity.name
                textIdActivity.text = register.activity.id.toString()
                textIdRegister.text = register.id.toString()
                val startTime = TextFormat.getLocalTime(register.start)
                textStart.text = startTime
                val endTime = TextFormat.getLocalTime(register.end)
                textEnd.text = endTime

                with(register){
                    val dateStart = "${start.dayOfMonth}-${start.month}-${start.year}"
                    val dateEnd = "${end.dayOfMonth}-${end.month}-${end.year}"
                    textDate.text = if (dateEnd == dateStart) dateStart
                                    else "$dateStart - $dateEnd"
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_register_timer, parent, false)
        return  RegisterHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RegisterHolder, position: Int) {
        holder.render(items[position])
    }
}