package com.jsus.tictacproject.code.objects

data class TimerItem(var id: Int,
                     var name: String,
                     var timer: IntervalTimer = IntervalTimer()
)
