package com.jsus.tictacproject.code.objects

import java.sql.Time

class TimeMeasurements (val id: Double,
                        val duration: Time,
                        val comment: String?,
                        activities: Activities
): Activities(activities.idA, activities.nameA, activities.descriptionA, activities.startTime, activities.endTime, activities.task) {
    //
    constructor(): this (0.0, Time(0), null,Activities())
}