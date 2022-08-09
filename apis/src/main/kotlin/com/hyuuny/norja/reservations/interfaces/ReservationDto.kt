package com.hyuuny.norja.reservations.interfaces

import com.hyuuny.norja.reservations.domain.ReservationCreateCommand
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

class ReservationDto

data class ReservationCreateDto(

    @field:Schema(description = "유저 아이디", example = "1", required = true)
    val userId: Long,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "객실 수", example = "10", required = true)
    val roomCount: Int,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,

    @field:Schema(description = "체크인", example = "2022-08-08", required = true)
    val checkIn: LocalDate,

    @field:Schema(description = "체크아웃", example = "2022-08-12", required = true)
    val checkOut: LocalDate,
) {
    fun toCommand() = ReservationCreateCommand(
        userId = this.userId,
        roomId = this.roomId,
        roomCount = this.roomCount,
        price = this.price,
        checkIn = this.checkIn,
        checkOut = this.checkOut,
    )
}
