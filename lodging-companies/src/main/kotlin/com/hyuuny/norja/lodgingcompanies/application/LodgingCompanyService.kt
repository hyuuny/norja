package com.hyuuny.norja.lodgingcompanies.application

import com.hyuuny.norja.lodgingcompanies.domain.*
import com.hyuuny.norja.lodgingcompanies.domain.collections.SearchedLodgingCompanies
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class LodgingCompanyService(
    private val lodgingCompanyStore: LodgingCompanyStore,
    private val lodgingCompanyReader: LodgingCompanyReader,
    private val lodgingCompanyDomainService: LodgingCompanyDomainService,
) {

    @Transactional
    fun createLodgingCompany(command: LodgingCompanyCreateCommand): Long {
        val newLodgingCompany = command.toEntity
        return lodgingCompanyStore.store(newLodgingCompany).id!!
    }

    fun retrieveLodgingCompany(
        searchQuery: LodgingCompanySearchQuery,
        pageable: Pageable,
    ): PageImpl<LodgingCompanyListingResponse> {
        val searched = lodgingCompanyReader.retrieveLodgingCompany(searchQuery, pageable)
        val searchedLodgingCompanies = SearchedLodgingCompanies(searched.content)
        return PageImpl(searchedLodgingCompanies.toPage(), pageable, searched.totalElements)
    }

    fun getLodgingCompany(id: Long): LodgingCompanyResponse {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        return LodgingCompanyResponse(loadedLodgingCompany)
    }

    fun getLodgingCompanyAndRooms(
        id: Long,
        searchQuery: DateSearchQuery
    ): LodgingCompanyAndRoomResponse =
        lodgingCompanyDomainService.getLodgingCompanyAndRoom(id, searchQuery)

    @Transactional
    fun updateLodgingCompany(
        id: Long,
        command: LodgingCompanyUpdateCommand
    ): LodgingCompanyResponse {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        command.update(loadedLodgingCompany)
        return getLodgingCompany(id)
    }

    @Transactional
    fun vacation(id: Long) {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        loadedLodgingCompany.vacation()
    }

    @Transactional
    fun deleteLodgingCompany(id: Long) {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        loadedLodgingCompany.delete()
    }

}