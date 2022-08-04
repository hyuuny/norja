package com.hyuuny.norja.reservation.infrastructure

import com.hyuuny.norja.reservation.domain.ReservationReader
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus

class ReservationReaderImpl(
    private val reservationRepository: ReservationRepository,
) : ReservationReader {

    override fun getReservation(id: Long) =
        reservationRepository.findByIdOrNull(id) ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "reservation.id.notFound",
            id
        )

}