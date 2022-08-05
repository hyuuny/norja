package com.hyuuny.norja.reservations.domain

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface ReservationReader {

    fun getReservation(id: Long): Reservation

    fun getCompletionReservation(id: Long): Reservation

    fun retrieveReservation(
        searchQuery: ReservationSearchQuery,
        pageable: Pageable
    ): PageImpl<SearchedReservationListing>

}
