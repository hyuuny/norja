package com.hyuuny.norja.rooms.application

import com.hyuuny.norja.rooms.domain.*
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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

    @Cacheable(value = ["roomCache"], key = "#id")
    fun getRoom(id: Long): RoomResponseDto {
        val loadedRoom = roomReader.getRoom(id)
        return RoomResponseDto(loadedRoom)
    }

    @CacheEvict(value = ["roomCache"], key = "#id")
    @Transactional
    fun updateRoom(id: Long, command: RoomUpdateCommand): RoomResponseDto {
        val loadedRoom = roomReader.getRoom(id)
        command.update(loadedRoom)
        return getRoom(id)
    }

    @CacheEvict(value = ["roomCache"], key = "#id")
    @Transactional
    fun deleteRoom(id: Long) = roomStore.delete(id)

}