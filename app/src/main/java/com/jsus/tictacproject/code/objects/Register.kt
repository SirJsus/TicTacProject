package com.jsus.tictacproject.code.objects

import java.time.LocalDateTime

class Register( val id: Int,
                val activity: Activity,
                val start: LocalDateTime,
                val end: LocalDateTime
){
    constructor(): this (0, Activity(), LocalDateTime.now(), LocalDateTime.now())

    fun create(id: Int, activity: Activity): Register{
        return Register(id,
                    Activity(activity.id, activity.name, activity.description),
                    activity.timer.start!!,
                    activity.timer.end!!
        )
    }
    override fun toString(): String {
        return "\nRegister ($id, $activity," +
                "${TextFormat.getLocalTime(start)}, " +
                "${TextFormat.getLocalTime(end)})"
    }
}
