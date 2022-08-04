package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.reservations.domain.ReservationReader
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ReservationReaderImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationReader {

    override fun getReservation(id: Long) =
        reservationRepository.findByIdOrNull(id) ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "reservation.id.notFound",
            id
        )

    override fun getCompletionReservation(id: Long) =
        reservationRepository.loadCompletionReservation(id) ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "reservation.id.notFound",
            id
        )

}