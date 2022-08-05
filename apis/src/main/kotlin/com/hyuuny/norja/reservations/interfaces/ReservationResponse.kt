package com.hyuuny.norja.reservations.interfaces

import com.hyuuny.norja.reservations.domain.ReservationInfo
import com.hyuuny.norja.reservations.domain.Status
import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationResponse(
    val id: Long,
    val userId: Long,
    val roomId: Long,
    val roomCount: Int,
    val status: Status,
    val price: Long,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val createdAt: LocalDateTime,
) {
    constructor(info: ReservationInfo) : this(
        id = info.id,
        userId = info.userId,
        roomId = info.roomId,
        roomCount = info.roomCount,
        status = info.status,
        price = info.price,
        checkIn = info.checkIn,
        checkOut = info.checkOut,
        createdAt = info.createdAt,
    )
}