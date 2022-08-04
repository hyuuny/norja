package com.hyuuny.norja.reservation.domain

interface ReservationReader {

    fun getReservation(id: Long): Reservation

}
