package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.*
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class LodgingCompanyReaderImpl(
    private val lodgingCompanyRepository: LodgingCompanyRepository,
) : LodgingCompanyReader {

    override fun getLodgingCompany(id: Long): LodgingCompany =
        lodgingCompanyRepository.findByIdOrNull(id) ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "lodgingCompany.id.notFound",
            id
        )

    override fun loadLodgingCompany(id: Long): SearchedLodgingCompany =
        lodgingCompanyRepository.loadLodgingCompany(id) ?: throw HttpStatusMessageException(
            HttpStatus.BAD_REQUEST,
            "lodgingCompany.id.notFound",
            id
        )

    override fun retrieveLodgingCompany(
        searchQuery: LodgingCompanySearchQuery,
        pageable: Pageable,
    ): PageImpl<SearchedLodgingCompanyListing> =
        lodgingCompanyRepository.retrieveLodgingCompanies(searchQuery, pageable)

}