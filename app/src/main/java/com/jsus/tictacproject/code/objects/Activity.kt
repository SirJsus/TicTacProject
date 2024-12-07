package com.jsus.tictacproject.code.objects


data class Activity(var id: Int,
                    var name: String,
                    var description: String?,
                    var timer: IntervalTimer = IntervalTimer()
)
