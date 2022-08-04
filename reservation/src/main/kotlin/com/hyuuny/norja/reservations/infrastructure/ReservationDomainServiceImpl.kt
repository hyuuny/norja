package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.reservations.domain.ReservationCountCommand
import com.hyuuny.norja.reservations.domain.ReservationDomainService
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ReservationDomainServiceImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationDomainService {


    override fun verifyReservation(command: ReservationCountCommand) {
        val result = reservationRepository.countRoomReservation(command)
        if (result.isFullReservation()) {
            throw HttpStatusMessageException(HttpStatus.BAD_REQUEST, "reservation.room.notValid")
        }
    }

}