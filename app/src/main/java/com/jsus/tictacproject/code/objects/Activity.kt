package com.jsus.tictacproject.code.objects


class Activity(var id: Int,
                    var name: String,
                    var description: String?,
                    var timer: TimeLapse = TimeLapse()
){
    constructor(): this (0, "", null)
    override fun toString(): String {
        return "\nActivity ($id, \"$name\", \"$description\", $timer)"
    }
}
