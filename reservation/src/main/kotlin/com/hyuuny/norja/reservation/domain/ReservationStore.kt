package com.hyuuny.norja.reservation.domain

interface ReservationStore {

    fun store(reservation: Reservation): Reservation

}