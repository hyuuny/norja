package com.hyuuny.norja.rooms.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.rooms.domain.QRoom.room
import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.Type
import com.querydsl.jpa.impl.JPAQueryFactory

class RoomRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Room::class.java), RoomRepositoryCustom {

    override fun loadRoomsByLodgingCompanyId(lodgingCompanyId: Long) =
        queryFactory.selectFrom(room)
            .where(lodgingCompanyIdEq(lodgingCompanyId))
            .groupBy(room.type)
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

}