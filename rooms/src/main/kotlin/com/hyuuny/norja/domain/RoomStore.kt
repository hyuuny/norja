package com.hyuuny.norja.domain

interface RoomStore {

    fun store(room: Room): Room

    fun delete(id: Long)

}