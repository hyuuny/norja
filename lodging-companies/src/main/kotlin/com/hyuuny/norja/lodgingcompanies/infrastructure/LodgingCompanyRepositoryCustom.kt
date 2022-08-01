package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompany
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanySearchQuery
import com.hyuuny.norja.lodgingcompanies.domain.SearchedLodgingCompanyListing
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface LodgingCompanyRepositoryCustom {

    fun loadLodgingCompany(id: Long): LodgingCompany?

    fun retrieveLodgingCompanies(
        searchQuery: LodgingCompanySearchQuery,
        pageable: Pageable,
    ): PageImpl<SearchedLodgingCompanyListing>

}