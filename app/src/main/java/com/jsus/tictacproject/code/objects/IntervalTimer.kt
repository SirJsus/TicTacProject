package com.jsus.tictacproject.code.objects

import java.time.LocalDateTime
import java.time.Duration

class IntervalTimer {

    private var startTime: LocalDateTime? = null
    private var interval: Long = 0

    val isRunning: Boolean
        get() = startTime != null

    fun start(now: LocalDateTime){
        if (startTime == null){
            startTime = now
        }
    }

    fun pause(){
        startTime?.let {
            interval += Duration.between(it, LocalDateTime.now()).toMillis()
            startTime = null
        }
    }

    fun reset(){
        startTime = null
        interval = 0
    }

    // Obtiene el tiempo acumulado, considerando el tiempo actual si está corriendo
    fun getElapsedTime(): Long {
        return if (startTime != null) {
            interval + Duration.between(startTime, LocalDateTime.now()).toMillis()
        } else {
            interval
        }
    }
}