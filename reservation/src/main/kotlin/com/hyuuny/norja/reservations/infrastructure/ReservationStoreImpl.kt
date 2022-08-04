package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.reservations.domain.Reservation
import com.hyuuny.norja.reservations.domain.ReservationStore
import org.springframework.stereotype.Component

@Component
class ReservationStoreImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationStore {

    override fun store(reservation: Reservation) = reservationRepository.save(reservation)

}