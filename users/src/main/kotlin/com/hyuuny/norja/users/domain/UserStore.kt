package com.hyuuny.norja.users.domain

interface UserStore {

    fun signUp(user: User): User

}