package com.hyuuny.norja.rooms.domain

interface RoomReader {

    fun getRoom(id: Long): Room

}