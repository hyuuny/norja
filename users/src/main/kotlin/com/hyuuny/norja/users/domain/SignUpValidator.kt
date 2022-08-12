package com.hyuuny.norja.users.domain

interface SignUpValidator {

    fun validate(user: User)

}