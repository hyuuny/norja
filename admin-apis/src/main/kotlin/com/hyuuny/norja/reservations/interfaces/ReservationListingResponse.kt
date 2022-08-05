package com.hyuuny.norja.reservations.interfaces

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.hyuuny.norja.reservations.domain.ReservationListingInfo
import com.hyuuny.norja.reservations.domain.Status
import java.time.LocalDate
import java.time.LocalDateTime

@JsonInclude(Include.NON_NULL)
class ReservationListingResponse(
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
    constructor(info: ReservationListingInfo) : this(
        id = info.id,
        code = info.code,
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