package com.hyuuny.norja.reservation.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationInfo(
    val id: Long,
    val code: String,
    val userId: Long,
    val roomId: Long,
    val status: Status,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val createAt: LocalDateTime,
) {
    constructor(entity: Reservation) : this(
        id = entity.id!!,
        code = entity.code,
        userId = entity.userId,
        roomId = entity.roomId,
        status = entity.status,
        checkIn = entity.checkIn,
        checkOut = entity.checkOut,
        createAt = entity.createdAt,
    )
}
