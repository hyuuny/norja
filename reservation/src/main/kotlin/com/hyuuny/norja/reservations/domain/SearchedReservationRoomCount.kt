package com.hyuuny.norja.reservations.domain

data class SearchedReservationRoomCount(
    val roomCount: Int = 0,
    val reservationCount: Long = 0,
) {
    fun isFullReservation() = this.roomCount.minus(this.reservationCount.toInt()) == 0
}
