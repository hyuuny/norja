package com.hyuuny.norja.rooms.domain

import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class SimpleRoomValidator(
    private val roomReader: RoomReader,
) : RoomValidator {

    override fun validate(room: Room) {
        val count = roomReader.getCountByType(room)

        if (count > 0) throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "room.type.duplicate"
        )
    }

}