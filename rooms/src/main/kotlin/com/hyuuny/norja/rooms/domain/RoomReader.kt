package com.hyuuny.norja.rooms.domain

import java.time.LocalDate

interface RoomReader {

    fun getRoom(id: Long): Room

    fun getRoomsByLodgingCompanyId(
        lodgingCompanyId: Long,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): List<RoomResponse>

    fun getCountByType(room: Room): Long

}