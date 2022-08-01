package com.hyuuny.norja.rooms.domain

interface RoomReader {

    fun getRoom(id: Long): Room

    fun getRoomsByLodgingCompanyId(lodgingCompanyId: Long): List<RoomInfo>

    fun getCountByType(room: Room): Long

}