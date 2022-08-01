package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.Type

interface RoomRepositoryCustom {

    fun loadRoomsByLodgingCompanyId(lodgingCompanyId: Long): List<Room>

    fun countByLodgingCompanyIdAndType(lodgingCompanyId: Long, type: Type): Long

}