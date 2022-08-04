package com.hyuuny.norja.reservations.domain

interface ReservationStore {

    fun store(reservation: Reservation): Reservation

}