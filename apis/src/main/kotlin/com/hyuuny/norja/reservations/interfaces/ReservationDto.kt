package com.hyuuny.norja.reservations.interfaces

import com.hyuuny.norja.reservations.domain.ReservationCreateCommand
import java.time.LocalDate

class ReservationDto

data class ReservationCreateDto(
    val userId: Long,
    val roomId: Long,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
) {
    fun toCommand() = ReservationCreateCommand(
        userId = this.userId,
        roomId = this.roomId,
        checkIn = this.checkIn,
        checkOut = this.checkOut,
    )
}
