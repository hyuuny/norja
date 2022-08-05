package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.reservations.domain.*
import com.hyuuny.norja.reservations.domain.QReservation.reservation
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.ObjectUtils.isEmpty
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReservationRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Reservation::class.java), ReservationRepositoryCustom {

    override fun loadCompletionReservation(id: Long): Reservation? {
        return queryFactory
            .selectFrom(reservation)
            .where(
                reservationIdEq(id),
                statusCompletion()
            )
            .fetchOne()
    }

    override fun countRoomReservation(command: ReservationCountCommand): Long {
        return queryFactory
            .select(reservation.id.count())
            .from(reservation)
            .where(
                reservationRoomIdEq(command.roomId),
                fromCheckIn(command.checkOut),
                toCheckOut(command.checkIn),
                statusCompletion(),
            )
            .fetchFirst()
    }

    override fun retrieveReservation(
        searchQuery: ReservationSearchQuery,
        pageable: Pageable
    ): PageImpl<SearchedReservationListing> {
        return applyPageImpl(
            pageable, queryFactory
                .select(
                    fields(
                        SearchedReservationListing::class.java,
                        reservation.id,
                        reservation.code,
                        reservation.userId,
                        reservation.roomId,
                        reservation.roomCount,
                        reservation.status,
                        reservation.price,
                        reservation.checkIn,
                        reservation.checkOut,
                        reservation.createdAt,
                    )
                )
                .from(reservation)
                .where(
                    idEq(searchQuery.id),
                    userIdEq(searchQuery.userId),
                    checkInEq(searchQuery.checkIn),
                    checkOutEq(searchQuery.checkOut),
                    statusEq(searchQuery.status),
                )
        )
    }

    private fun reservationIdEq(id: Long) = reservation.id.eq(id)

    private fun reservationRoomIdEq(roomId: Long) = reservation.roomId.eq(roomId)

    private fun fromCheckIn(checkOut: LocalDate) = reservation.checkIn.lt(checkOut)

    private fun toCheckOut(checkIn: LocalDate) = reservation.checkOut.gt(checkIn)

    private fun statusCompletion() = reservation.status.eq(Status.COMPLETION)

    private fun idEq(id: Long?) = if (isEmpty(id)) null else reservation.id.eq(id)

    private fun userIdEq(userId: Long?) =
        if (isEmpty(userId)) null else reservation.userId.eq(userId)

    private fun checkInEq(checkIn: String?) =
        if (isEmpty(checkIn)) null else reservation.checkIn.eq(
            LocalDate.parse(checkIn, DateTimeFormatter.ISO_DATE)
        )

    private fun checkOutEq(checkOut: String?) =
        if (isEmpty(checkOut)) null else reservation.checkOut.eq(
            LocalDate.parse(checkOut, DateTimeFormatter.ISO_DATE)
        )

    private fun statusEq(status: Status?) =
        if (isEmpty(status)) null else reservation.status.eq(status)

}