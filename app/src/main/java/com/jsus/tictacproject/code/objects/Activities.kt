package com.jsus.tictacproject.code.objects

import java.util.Date

open class Activities (val idA: Double,
                       val nameA: String,
                       val descriptionA: String?,
                       val startTime: Date,
                       val endTime: Date?,
                       val task: Task
): Task(task.idT, task.nameT, task.descriptionT, task.user) {
    //
    constructor(): this (0.0, "", null, Date(), null, Task())
}