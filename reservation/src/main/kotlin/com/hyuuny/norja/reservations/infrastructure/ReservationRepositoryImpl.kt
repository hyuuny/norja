package com.hyuuny.norja.reservations.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.reservations.domain.QReservation.reservation
import com.hyuuny.norja.reservations.domain.Reservation
import com.hyuuny.norja.reservations.domain.ReservationCountCommand
import com.hyuuny.norja.reservations.domain.SearchedReservationRoomCount
import com.hyuuny.norja.reservations.domain.Status
import com.hyuuny.norja.rooms.domain.QRoom.room
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate

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

    override fun countRoomReservation(command: ReservationCountCommand): SearchedReservationRoomCount {
        return queryFactory.select(
            fields(
                SearchedReservationRoomCount::class.java,
                ExpressionUtils.`as`(
                    JPAExpressions.select(room.roomCount)
                        .from(room)
                        .where(roomIdEq(command.roomId)), "roomCount"
                ),
                reservation.id.count().`as`("reservationCount"),
            )
        )
            .from(reservation)
            .where(
                reservationRoomIdEq(command.roomId),
                fromCheckIn(command.checkOut),
                toCheckOut(command.checkIn),
                statusCompletion(),
            )
            .fetchFirst()
    }

    private fun reservationIdEq(id: Long) = reservation.id.eq(id)

    private fun reservationRoomIdEq(roomId: Long) = reservation.roomId.eq(roomId)

    private fun roomIdEq(roomId: Long) = room.id.eq(roomId)

    private fun fromCheckIn(checkOut: LocalDate) = reservation.checkIn.lt(checkOut)

    private fun toCheckOut(checkIn: LocalDate) = reservation.checkOut.gt(checkIn)

    private fun statusCompletion() = reservation.status.eq(Status.COMPLETION)


}