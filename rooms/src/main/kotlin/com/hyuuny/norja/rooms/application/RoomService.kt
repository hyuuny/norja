package com.hyuuny.norja.rooms.application

import com.hyuuny.norja.rooms.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class RoomService(
    private val roomReader: RoomReader,
    private val roomStore: RoomStore,
    private val roomValidator: RoomValidator,
) {

    @Transactional
    fun createRoom(command: RoomCreateCommand): Long {
        val newRoom = command.toEntity
        roomValidator.validate(newRoom)
        return roomStore.store(newRoom).id!!
    }

    fun getRoom(id: Long): RoomResponse {
        val loadedRoom = roomReader.getRoom(id)
        return RoomResponse(loadedRoom)
    }

    @Transactional
    fun updateRoom(id: Long, command: RoomUpdateCommand): RoomResponse {
        val loadedRoom = roomReader.getRoom(id)
        command.update(loadedRoom)
        return getRoom(id)
    }

    @Transactional
    fun deleteRoom(id: Long) = roomStore.delete(id)

}