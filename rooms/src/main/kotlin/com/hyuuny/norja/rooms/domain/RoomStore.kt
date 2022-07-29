package com.hyuuny.norja.rooms.domain

interface RoomStore {

    fun store(room: Room): Room

    fun delete(id: Long)

}