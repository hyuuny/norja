package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.RoomStore
import org.springframework.stereotype.Component

@Component
class RoomStoreImpl(
    private val roomRepository: RoomRepository,
): RoomStore {

    override fun store(room: Room) = roomRepository.save(room)

    override fun delete(id: Long) = roomRepository.deleteById(id)

}