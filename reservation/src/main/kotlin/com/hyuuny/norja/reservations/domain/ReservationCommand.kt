package com.hyuuny.norja.reservations.domain

import java.time.LocalDate

data class ReservationCreateCommand(
    val userId: Long,
    val roomId: Long,
    val roomCount: Int,
    val price: Long,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
) {
    val toEntity = Reservation.create(
        userId = this.userId,
        roomId = this.roomId,
        roomCount = this.roomCount,
        price = this.price,
        checkIn = this.checkIn,
        checkOut = this.checkOut,
    )

    val toCountCommand = ReservationCountCommand(
        roomId = this.roomId,
        roomCount = this.roomCount,
        checkIn = this.checkIn,
        checkOut = this.checkOut,
    )
}

data class ReservationCountCommand(
    val roomId: Long,
    val roomCount: Int,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
)