package com.hyuuny.norja.reservation.infrastructure

import com.hyuuny.norja.reservation.domain.Reservation
import com.hyuuny.norja.reservation.domain.ReservationStore

class ReservationStoreImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationStore {

    override fun store(reservation: Reservation) = reservationRepository.save(reservation)

}