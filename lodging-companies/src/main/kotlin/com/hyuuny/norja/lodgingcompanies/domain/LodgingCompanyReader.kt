package com.hyuuny.norja.lodgingcompanies.domain

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface LodgingCompanyReader {

    fun getLodgingCompany(id: Long): LodgingCompany

    fun loadLodgingCompany(id: Long): SearchedLodgingCompany

    fun retrieveLodgingCompany(
        searchQuery: LodgingCompanySearchQuery,
        pageable: Pageable,
    ): PageImpl<SearchedLodgingCompanyListing>

}