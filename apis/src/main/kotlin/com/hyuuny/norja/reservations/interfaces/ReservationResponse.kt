package com.hyuuny.norja.reservations.interfaces

import com.hyuuny.norja.reservations.domain.ReservationInfo
import com.hyuuny.norja.reservations.domain.Status
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationResponse(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "유저 아이디", example = "1", required = true)
    val userId: Long,

    @field:Schema(description = "객실 아이디", example = "1", required = true)
    val roomId: Long,

    @field:Schema(description = "객실 수", example = "10", required = true)
    val roomCount: Int,

    @field:Schema(description = "상태", example = "OPEN", required = true)
    val status: Status,

    @field:Schema(description = "금액", example = "130000", required = true)
    val price: Long,


    @field:Schema(description = "체크인", example = "2022-08-08", required = true)
    val checkIn: LocalDate,

    @field:Schema(description = "체크아웃", example = "2022-08-12", required = true)
    val checkOut: LocalDate,

    @field:Schema(description = "등록일", example = "2022-08-08T21:51:00.797659")
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