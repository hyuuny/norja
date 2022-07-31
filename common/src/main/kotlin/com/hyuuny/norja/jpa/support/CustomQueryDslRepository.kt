package com.hyuuny.norja.jpa.support

import com.querydsl.jpa.JPQLQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
abstract class CustomQueryDslRepository(domainClass: Class<*>?) :
    QuerydslRepositorySupport(domainClass!!) {

    fun <T> applyPageImpl(pageable: Pageable, query: JPQLQuery<T>): PageImpl<T> {
        val totalCount = query.fetchCount()
        val results = querydsl!!.applyPagination(pageable, query).fetch()
        return PageImpl(results, pageable, totalCount)
    }

}