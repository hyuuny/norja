package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.reservations.domain.Reservation
import com.hyuuny.norja.reservations.domain.ReservationCountCommand
import com.hyuuny.norja.reservations.domain.SearchedReservationRoomCount

interface ReservationRepositoryCustom {

    fun loadCompletionReservation(id: Long): Reservation?

    fun countRoomReservation(command: ReservationCountCommand): SearchedReservationRoomCount

}