package com.hyuuny.norja.reservations.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class SearchedReservationListing(
    val id: Long? = null,
    val code: String? = null,
    val userId: Long? = null,
    val roomId: Long? = null,
    val roomCount: Int? = null,
    val status: Status? = null,
    val price: Long? = null,
    val checkIn: LocalDate? = null,
    val checkOut: LocalDate? = null,
    val createdAt: LocalDateTime? = null,
)
