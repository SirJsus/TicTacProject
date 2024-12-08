package com.jsus.tictacproject.code.objects

import com.jsus.tictacproject.code.db.DBHelper
import java.time.LocalDateTime
import kotlin.random.Random

class Register( val id: Int,
                val description: String?,
                val activity: Activity,
                val start: LocalDateTime,
                val end: LocalDateTime
){
    constructor(): this (0, null, Activity(), LocalDateTime.now(), LocalDateTime.now())

    fun create(activity: Activity, db: DBHelper): Register{
        var newID: Int
        do {
            newID = Random.nextInt()
            val isFree = db.getRegisterByID(newID, activity)
        } while (isFree == Register())
        val newRegister = Register(newID, null,
                                Activity(activity.id, activity.name, activity.description),
                                activity.timer.start!!,
                                activity.timer.end!!)
        db.insertRegister(newRegister)
        return newRegister
    }

    override fun toString(): String {
        return "\nRegister ($id, $description, $activity," +
                "${TextFormat.getLocalTime(start)}, " +
                "${TextFormat.getLocalTime(end)})"
    }
}
