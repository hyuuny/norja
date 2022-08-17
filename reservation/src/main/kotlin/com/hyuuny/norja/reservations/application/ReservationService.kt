package com.hyuuny.norja.reservations.application

import com.hyuuny.norja.reservations.domain.*
import com.hyuuny.norja.reservations.domain.collections.SearchedReservations
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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
        reservationDomainService.verifyReservation(command.toCountCommand)
        return reservationStore.store(newReservation).id!!
    }

    fun getReservation(id: Long): ReservationResponse {
        val loadedReservation = reservationReader.getReservation(id)
        return ReservationResponse(loadedReservation)
    }

    @Transactional
    fun requestCancel(id: Long) {
        val loadedReservation = reservationReader.getCompletionReservation(id)
        loadedReservation.cancel()
    }

    fun retrieveReservation(
        searchQuery: ReservationSearchQuery,
        pageable: Pageable
    ): PageImpl<ReservationListingResponse> {
        val searched = reservationReader.retrieveReservation(searchQuery, pageable)
        val searchedReservations = SearchedReservations(searched.content)
        return PageImpl(searchedReservations.toPage(), pageable, searched.totalElements)
    }

}