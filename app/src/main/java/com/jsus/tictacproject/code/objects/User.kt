package com.jsus.tictacproject.code.objects

open class User (val idU: Double,
                 val username: String,
                 val email: String?,
                 val password: String?
) {
    //
    constructor(): this (0.0, "", null, null)
}