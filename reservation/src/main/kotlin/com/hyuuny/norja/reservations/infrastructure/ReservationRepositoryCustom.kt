package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.reservations.domain.Reservation
import com.hyuuny.norja.reservations.domain.ReservationCountCommand
import com.hyuuny.norja.reservations.domain.ReservationSearchQuery
import com.hyuuny.norja.reservations.domain.SearchedReservationListing
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface ReservationRepositoryCustom {

    fun loadCompletionReservation(id: Long): Reservation?

    fun countRoomReservation(command: ReservationCountCommand): Long

    fun retrieveReservation(
        searchQuery: ReservationSearchQuery,
        pageable: Pageable,
    ): PageImpl<SearchedReservationListing>

}