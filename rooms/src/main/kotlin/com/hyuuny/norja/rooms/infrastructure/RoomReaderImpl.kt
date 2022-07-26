package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.RoomReader
import com.hyuuny.norja.rooms.domain.RoomResponseDto
import com.hyuuny.norja.rooms.domain.SearchedRoom
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDate

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

    override fun getRoomsByLodgingCompanyId(
        lodgingCompanyId: Long,
        checkIn: LocalDate,
        checkOut: LocalDate
    ): List<RoomResponseDto> {
        val loadedRoom =
            roomRepository.loadRoomsByLodgingCompanyId(lodgingCompanyId, checkIn, checkOut)

        return loadedRoom.map(::toResponse)
            .sortedBy(RoomResponseDto::price)
            .toList()
    }

    private fun toResponse(searchedRoom: SearchedRoom) =
        RoomResponseDto(searchedRoom.room!!, searchedRoom.remainingRoomCount().toString())

    override fun getCountByType(room: Room) =
        roomRepository.countByLodgingCompanyIdAndType(room.lodgingCompanyId, room.type)

}