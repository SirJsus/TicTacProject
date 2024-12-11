package com.jsus.tictacproject.code.objects

import com.jsus.tictacproject.code.db.DBHelper
import kotlin.random.Random


class Activity(var id: Int,
                    var name: String,
                    var description: String?,
                    var timer: TimeLapse = TimeLapse()
){
    constructor(): this (0, "", null)

    fun create(name: String, desc: String?, db: DBHelper): Activity{
        var newID: Int
        do {
            newID = Random.nextInt()
            val isFree = db.getActivityByID(newID)
        } while (isFree == Activity())

        val newActivity = Activity(newID, name, desc)
        db.insertActivity(newActivity)
        return newActivity
    }

    fun getList(db: DBHelper): MutableList<Activity>{
        return db.getActivityList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Activity) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (timer != other.timer) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 29 * result + name.hashCode()
        result = 29 * result + description.hashCode()
        result = 29 * result + timer.hashCode()
        return result
    }

    override fun toString(): String {
        return "\nActivity ($id, $name, $description, $timer)"
    }
}
