package com.hyuuny.norja.users.domain

interface UserReader {

    fun getUser(id: Long): User

}