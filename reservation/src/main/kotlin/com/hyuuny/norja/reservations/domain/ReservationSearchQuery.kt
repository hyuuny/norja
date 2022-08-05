package com.hyuuny.norja.reservations.domain

data class ReservationSearchQuery(
    val id: Long? = null,
    val userId: Long? = null,
    val checkIn: String? = null,
    val checkOut: String? = null,
    val status: Status? = null,
)
