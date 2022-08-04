package com.hyuuny.norja.reservations.domain

interface ReservationDomainService {

    fun verifyReservation(command: ReservationCountCommand)

}