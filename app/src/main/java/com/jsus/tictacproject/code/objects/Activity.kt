package com.jsus.tictacproject.code.objects

import android.util.Log
import com.jsus.tictacproject.code.db.DBHelper
import java.time.LocalDateTime
import kotlin.random.Random


class Activity(var id: Int,
                    var name: String,
                    var description: String?,
                    var archived: Boolean = false,
                    var timer: TimeLapse = TimeLapse()
){
    constructor(): this (0, "", null, true)

    fun create(name: String, desc: String?, db: DBHelper): Activity{
        var newID: Int
        do {
            newID = Random.nextInt()
            val isFree = db.getActivityByID(newID)
        } while (isFree != Activity())

        val newActivity = Activity(newID, name, desc)
        db.insertActivity(newActivity)
        return newActivity
    }

    fun getList(db: DBHelper): MutableList<Activity>{
        return db.getActivityList()
    }

    fun startTime(activity: Activity, now: LocalDateTime, db: DBHelper){
        //
        activity.timer.start(now)
        Log.d("tictac_TimerAdapter", "startTimer, activity: $activity")
        db.insertNow(activity)
    }

    fun stopTimer(activity: Activity, now: LocalDateTime, db: DBHelper) {
        if (activity.timer.isRunning){
            activity.timer.end(now)
            val newReg = Register().create(activity, db)
            db.deleteNow(1, newReg.activity)
            Log.d("tictac_TimerAdapter", "stopTimer, activity: $activity")
            activity.timer.reset()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Activity) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (archived != other.archived) return false
        if (timer != other.timer) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 29 * result + name.hashCode()
        result = 29 * result + description.hashCode()
        result = 29 * result + archived.hashCode()
        result = 29 * result + timer.hashCode()
        return result
    }

    override fun toString(): String {
        return "\nActivity ($id, $name, $description, $archived, " +
                "${timer.start?.let { TextFormat.getLocalTime(it) }}, " +
                "${timer.end?.let { TextFormat.getLocalTime(it) }}, ${timer.isRunning})"
    }
}
