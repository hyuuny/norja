package com.hyuuny.norja.reservation.infrastructure

import com.hyuuny.norja.reservation.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, Long> {
}