package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.reservations.domain.QReservation.reservation
import com.hyuuny.norja.reservations.domain.Status
import com.hyuuny.norja.rooms.domain.QRoom.room
import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.SearchedRoom
import com.hyuuny.norja.rooms.domain.Type
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate

class RoomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Room::class.java), RoomRepositoryCustom {

    override fun loadRoomsByLodgingCompanyId(
        lodgingCompanyId: Long,
        checkIn: LocalDate,
        checkOut: LocalDate
    ) = queryFactory.select(
        fields(
            SearchedRoom::class.java,
            ExpressionUtils.`as`(room, "room"),
            ExpressionUtils.`as`(
                JPAExpressions.select(reservation.id.count())
                    .from(reservation)
                    .where(
                        reservation.roomId.eq(room.id),
                        fromCheckIn(checkOut),
                        toCheckOut(checkIn),
                        statusCompletion(),
                    ), "reservedRoomCount"
            ),
            room.type,
        )
    )
        .from(room)
        .where(lodgingCompanyIdEq(lodgingCompanyId))
        .groupBy(room.type, room.id)
        .fetch().toList()

    override fun countByLodgingCompanyIdAndType(lodgingCompanyId: Long, type: Type) =
        queryFactory
            .select(room.id.count())
            .from(room)
            .where(
                lodgingCompanyIdEq(lodgingCompanyId),
                room.type.eq(type)
            )
            .fetchOne()!!

    private fun lodgingCompanyIdEq(lodgingCompanyId: Long) =
        room.lodgingCompanyId.eq(lodgingCompanyId)

    private fun fromCheckIn(checkOut: LocalDate) = reservation.checkIn.lt(checkOut)

    private fun toCheckOut(checkIn: LocalDate) = reservation.checkOut.gt(checkIn)

    private fun statusCompletion() = reservation.status.eq(Status.COMPLETION)

}