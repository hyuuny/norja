package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.reservations.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
}