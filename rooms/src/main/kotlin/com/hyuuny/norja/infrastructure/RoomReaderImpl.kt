package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Room
import com.hyuuny.norja.domain.RoomReader
import com.hyuuny.norja.domain.RoomRepository
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

}