package com.jsus.tictacproject.code.objects

import java.time.LocalDateTime
import java.time.Duration

class TimeLapse {

    var start: LocalDateTime? = null
    var end: LocalDateTime? = null
    private var interval: Long = 0

    val isRunning: Boolean
        get() = start != null

    fun start(now: LocalDateTime){
        if (start == null){
            start = now
            end = null
        }
    }

    fun end(now: LocalDateTime){
        if (start != null){
            end = now
        }
    }

    fun pause(){
        start?.let {
            interval += Duration.between(it, LocalDateTime.now()).toMillis()
            start = null
        }
    }

    fun reset(){
        start = null
        interval = 0
    }

    // Obtiene el tiempo acumulado, considerando el tiempo actual si está corriendo
    fun getElapsedTime(): Long {
        return if (start != null) {
            interval + Duration.between(start, LocalDateTime.now()).toMillis()
        } else {
            interval
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TimeLapse) return false

        if (start != other.start) return false
        if (end != other.end) return false
        if (interval != other.interval) return false
        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 29 * result + end.hashCode()
        result = 29 * result + interval.hashCode()
        return result
    }

    override fun toString(): String {
        return "\nTimeLapse (${start?.let { TextFormat.getLocalTime(it) }}, " +
                "${end?.let { TextFormat.getLocalTime(it) }}, " +
                "$interval, $isRunning)"
    }

}