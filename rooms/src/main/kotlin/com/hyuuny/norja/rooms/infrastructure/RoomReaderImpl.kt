package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.RoomInfo
import com.hyuuny.norja.rooms.domain.RoomReader
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class RoomReaderImpl(
    private val roomRepository: RoomRepository,
) : RoomReader {

    override fun getRoom(id: Long): Room =
        roomRepository.findByIdOrNull(id) ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "room.id.notFound",
            id
        )

    override fun getRoomsByLodgingCompanyId(lodgingCompanyId: Long): List<RoomInfo> {
        val loadedRoom = roomRepository.loadRoomsByLodgingCompanyId(lodgingCompanyId)
        return loadedRoom.stream()
            .map(::RoomInfo)
            .toList()
    }

    override fun getCountByType(room: Room) =
        roomRepository.countByLodgingCompanyIdAndType(room.lodgingCompanyId, room.type)

}