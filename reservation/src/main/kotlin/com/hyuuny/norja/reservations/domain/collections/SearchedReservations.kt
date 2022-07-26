package com.hyuuny.norja.reservations.domain.collections

import com.hyuuny.norja.reservations.domain.ReservationListingResponseDto
import com.hyuuny.norja.reservations.domain.SearchedReservationListing

data class SearchedReservations(
    val reservations: List<SearchedReservationListing>,
) {
    fun toPage(): List<ReservationListingResponseDto> = this.reservations.stream()
        .map { ReservationListingResponseDto(it) }
        .toList()
}
