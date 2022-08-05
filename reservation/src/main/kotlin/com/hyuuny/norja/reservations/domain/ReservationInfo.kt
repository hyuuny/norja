package com.hyuuny.norja.reservations.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationInfo(
    val id: Long,
    val code: String,
    val userId: Long,
    val roomId: Long,
    val roomCount: Int,
    val status: Status,
    val price: Long,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val createdAt: LocalDateTime,
) {
    constructor(entity: Reservation) : this(
        id = entity.id!!,
        code = entity.code,
        userId = entity.userId,
        roomId = entity.roomId,
        roomCount = entity.roomCount,
        status = entity.status,
        price = entity.price,
        checkIn = entity.checkIn,
        checkOut = entity.checkOut,
        createdAt = entity.createdAt,
    )
}
