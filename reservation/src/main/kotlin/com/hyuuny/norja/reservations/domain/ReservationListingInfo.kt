package com.hyuuny.norja.reservations.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class ReservationListingInfo(
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
    constructor(searched: SearchedReservationListing) : this(
        id = searched.id!!,
        code = searched.code!!,
        userId = searched.userId!!,
        roomId = searched.roomId!!,
        roomCount = searched.roomCount!!,
        status = searched.status!!,
        price = searched.price!!,
        checkIn = searched.checkIn!!,
        checkOut = searched.checkOut!!,
        createdAt = searched.createdAt!!,
    )
}
