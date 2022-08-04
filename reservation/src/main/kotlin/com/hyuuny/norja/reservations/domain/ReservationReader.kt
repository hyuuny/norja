package com.hyuuny.norja.reservations.domain

interface ReservationReader {

    fun getReservation(id: Long): Reservation

    fun getCompletionReservation(id: Long) : Reservation

}
