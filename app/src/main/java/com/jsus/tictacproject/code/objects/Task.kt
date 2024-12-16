package com.jsus.tictacproject.code.objects

import android.util.Log
import com.jsus.tictacproject.code.db.DBHelper
import kotlin.random.Random

class Task(var id: Int,
           var name: String,
           var desc: String?,
           var listActivity: List<Activity> = emptyList(),
           var arch: Boolean = false,
) {
    constructor(): this (0, "", null)

    fun create(name: String, desc: String?, db: DBHelper, list: List<Activity> = emptyList()): Task{
        var newID: Int
        do {
            newID = Random.nextInt()
            val isFree = db.getTaskByID(newID)
        } while (isFree != Task())

        val newTask = Task(newID, name, desc, list)
        db.insertTask(newTask)
        return newTask
    }

    fun getList(db: DBHelper): MutableList<Task>{
        return db.getTaskList()
    }

    fun start(task: Task, db: DBHelper){
        db.insertNowTask(task)
        Log.d("tictac_Task", "setTaskNow, Task: $task")
    }

    fun stop(db: DBHelper){
        db.deleteNow(2)
    }

    fun archived(task: Task, db: DBHelper) {
        db.archivedTask(task)
    }

    fun getNow(db: DBHelper): Task{
        return db.getNowTask()
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