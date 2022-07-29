package com.hyuuny.norja.rooms.domain

import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, Long> {
}