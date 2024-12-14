package com.jsus.tictacproject.code.objects

import java.time.LocalDateTime

class TextFormat {

    companion object {
        fun getLocalTime(local: LocalDateTime): String{
            val hour = local.hour
            val minute = local.minute
            val second = local.second
            return String.format("%02d:%02d:%02d", hour, minute, second)
        }

        fun getLongTime(milliseconds: Long): String {
            val mill = (milliseconds / 10) % 100
            val seconds = (milliseconds / 1000) % 60
            val minutes = (milliseconds / (1000 * 60)) % 60
            val hours = (milliseconds / (1000 * 60 * 60))
            return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, mill)
        }

        fun getTime(milliseconds: Long): String {
            val mill = (milliseconds / 10) % 100
            val seconds = (milliseconds / 1000) % 60
            val minutes = (milliseconds / (1000 * 60)) % 60
            val hours = (milliseconds / (1000 * 60 * 60))
            val text = if (hours > 0) String.format("%02d hr. %02d min. %02d sec.", hours, minutes, seconds)
            else if (minutes > 0) String.format("%02d min. %02d sec.", minutes, seconds)
            else String.format("%02d sec. %02d mill", seconds, mill)
            return text
        }
    }
}