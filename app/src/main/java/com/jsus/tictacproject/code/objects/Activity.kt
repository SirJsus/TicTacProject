package com.jsus.tictacproject.code.objects

import com.jsus.tictacproject.code.db.DBHelper
import kotlin.random.Random


class Activity(var id: Int,
                    var name: String,
                    var description: String?,
                    var timer: TimeLapse = TimeLapse()
){
    constructor(): this (0, "", null)
    override fun toString(): String {
        return "\nActivity ($id, $name, $description, $timer)"
    }

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
}
