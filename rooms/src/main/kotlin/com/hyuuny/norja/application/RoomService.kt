package com.hyuuny.norja.application

import com.hyuuny.norja.domain.RoomReader
import com.hyuuny.norja.domain.RoomStore
import com.hyuuny.norja.domain.command.RoomCreateCommand
import com.hyuuny.norja.domain.command.RoomUpdateCommand
import com.hyuuny.norja.domain.info.RoomInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class RoomService(
    private val roomReader: RoomReader,
    private val roomStore: RoomStore,
) {

    @Transactional
    fun createRoom(command: RoomCreateCommand): Long {
        val newRoom = command.toEntity
        return roomStore.store(newRoom).id!!
    }

    fun getRoom(id: Long): RoomInfo {
        val loadedRoom = roomReader.getRoom(id)
        return RoomInfo(loadedRoom)
    }

    @Transactional
    fun updateRoom(id: Long, command: RoomUpdateCommand): RoomInfo {
        val loadedRoom = roomReader.getRoom(id)
        command.update(loadedRoom)
        return getRoom(id)
    }

    @Transactional
    fun deleteRoom(id: Long) = roomStore.delete(id)

}