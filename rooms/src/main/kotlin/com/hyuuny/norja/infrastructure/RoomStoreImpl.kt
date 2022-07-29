package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.Room
import com.hyuuny.norja.domain.RoomRepository
import com.hyuuny.norja.domain.RoomStore
import org.springframework.stereotype.Component

@Component
class RoomStoreImpl(
    private val roomRepository: RoomRepository,
): RoomStore {

    override fun store(room: Room) = roomRepository.save(room)

    override fun delete(id: Long) = roomRepository.deleteById(id)

}