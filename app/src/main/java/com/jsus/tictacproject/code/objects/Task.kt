package com.jsus.tictacproject.code.objects

open class Task (val idT: Double,
                 val nameT: String,
                 val descriptionT: String?,
                 val user: User
): User(user.idU, user.username, user.email, user.password) {
    //
    constructor(): this (0.0, "", null, User())
}