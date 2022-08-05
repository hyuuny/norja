package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.rooms.domain.SearchedRoom
import com.hyuuny.norja.rooms.domain.Type
import java.time.LocalDate

interface RoomRepositoryCustom {

    fun loadRoomsByLodgingCompanyId(
        lodgingCompanyId: Long,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): List<SearchedRoom>

    fun countByLodgingCompanyIdAndType(lodgingCompanyId: Long, type: Type): Long

}