package com.hyuuny.norja.reservations.application

import com.hyuuny.norja.reservations.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ReservationService(
    private val reservationStore: ReservationStore,
    private val reservationReader: ReservationReader,
    private val reservationDomainService: ReservationDomainService,
) {

    @Transactional
    fun createReservation(command: ReservationCreateCommand): Long {
        val newReservation = command.toEntity
        newReservation.validate()

        reservationDomainService.verifyReservation(
            ReservationCountCommand(command.roomId, command.checkIn, command.checkOut)
        )
        return reservationStore.store(newReservation).id!!
    }

    fun getReservation(id: Long): ReservationInfo {
        val loadedReservation = reservationReader.getReservation(id)
        return ReservationInfo(loadedReservation)
    }

    @Transactional
    fun requestCancel(id: Long) {
        val loadedReservation = reservationReader.getCompletionReservation(id)
        loadedReservation.cancel()
    }

}