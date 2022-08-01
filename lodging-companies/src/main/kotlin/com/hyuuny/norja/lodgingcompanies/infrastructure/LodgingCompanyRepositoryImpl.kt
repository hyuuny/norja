package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.jpa.support.CustomQueryDslRepository
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompany
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanySearchQuery
import com.hyuuny.norja.lodgingcompanies.domain.QLodgingCompany.lodgingCompany
import com.hyuuny.norja.lodgingcompanies.domain.SearchedLodgingCompanyListing
import com.hyuuny.norja.lodgingcompanies.domain.Status
import com.hyuuny.norja.rooms.domain.QRoom.room
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.ObjectUtils.isEmpty

class LodgingCompanyRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(LodgingCompany::class.java), LodgingCompanyRepositoryCustom {
    override fun loadLodgingCompany(id: Long) = queryFactory
        .selectFrom(lodgingCompany)
        .from(lodgingCompany)
        .where(
            lodgingCompany.id.eq(id),
            lodgingCompany.status.eq(Status.OPEN)
        )
        .fetchOne()

    override fun retrieveLodgingCompanies(
        searchQuery: LodgingCompanySearchQuery,
        pageable: Pageable,
    ): PageImpl<SearchedLodgingCompanyListing> = applyPageImpl(
        pageable, queryFactory
            .select(
                fields(
                    SearchedLodgingCompanyListing::class.java,
                    lodgingCompany.id,
                    lodgingCompany.type,
                    lodgingCompany.status,
                    lodgingCompany.name,
                    lodgingCompany.thumbnail,
                    lodgingCompany.address,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(room.price.min())
                            .from(room)
                            .where(room.lodgingCompanyId.eq(lodgingCompany.id)), "price"
                    ),
                    lodgingCompany.searchTag,
                    lodgingCompany.createdAt,
                )
            )
            .from(lodgingCompany)
            .where(
                searchTagContains(searchQuery.searchTag),
                addressContains(searchQuery.address),
            )
    )

    private fun searchTagContains(searchTag: String?) =
        if (isEmpty(searchTag)) null else lodgingCompany.searchTag.contains(searchTag)

    private fun addressContains(address: String?) =
        if (isEmpty(address)) null else lodgingCompany.address.address.contains(address)

}