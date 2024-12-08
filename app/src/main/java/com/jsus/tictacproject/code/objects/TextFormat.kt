package com.jsus.tictacproject.code.objects

import java.time.LocalDateTime

class TextFormat {

    companion object {
        fun getTimeFormat(local: LocalDateTime): String{
            val hour = local.hour
            val minute = local.minute
            val second = local.second
            return String.format("%02d:%02d:%02d", hour, minute, second)
        }
    }

}