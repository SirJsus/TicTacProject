package com.jsus.tictacproject.code.objects

import com.jsus.tictacproject.code.db.DBHelper
import kotlin.random.Random

class Task(var id: Int,
           var name: String,
           var desc: String?,
           var arch: Boolean = false,
           var listActivity: List<Activity> = emptyList()) {
    constructor(): this (0, "", null, true)

    fun create(name: String, desc: String?, db: DBHelper): Task{
        var newID: Int
        do {
            newID = Random.nextInt()
            val isFree = db.getTaskByID(newID)
        } while (isFree != Task())

        val newTask = Task(newID, name, desc)
        db.insertTask(newTask)
        return newTask
    }

    fun getList(db: DBHelper): MutableList<Task>{
        return db.getTaskList()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false

        if (id != other.id) return false
        if (name != other.name) return false
        if (desc != other.desc) return false
        if (arch != other.arch) return false
        if (listActivity != other.listActivity) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 29 * result + name.hashCode()
        result = 29 * result + desc.hashCode()
        result = 29 * result + arch.hashCode()
        result = 29 * result + listActivity.hashCode()
        return result
    }

    override fun toString(): String {
        return "\nTask ($id, $name, $desc, $arch, $listActivity)"
    }
}