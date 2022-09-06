package com.hyuuny.norja.lodgingcompanies.application

import com.hyuuny.norja.lodgingcompanies.domain.*
import com.hyuuny.norja.lodgingcompanies.domain.collections.SearchedLodgingCompanies
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    ): PageImpl<LodgingCompanyListingResponseDto> {
        val searched = lodgingCompanyReader.retrieveLodgingCompany(searchQuery, pageable)
        val searchedLodgingCompanies = SearchedLodgingCompanies(searched.content)
        return PageImpl(searchedLodgingCompanies.toPage(), pageable, searched.totalElements)
    }

    @Cacheable(value = ["lodgingCompanyCache"], key = "#id")
    fun getLodgingCompany(id: Long): LodgingCompanyResponseDto {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        return LodgingCompanyResponseDto(loadedLodgingCompany)
    }

    fun getLodgingCompanyAndRooms(
        id: Long,
        dateSearchQuery: DateSearchQuery
    ): LodgingCompanyAndRoomResponseDto =
        lodgingCompanyDomainService.getLodgingCompanyAndRoom(id, dateSearchQuery)

    @CacheEvict(value = ["lodgingCompanyCache"], key = "#id")
    @Transactional
    fun updateLodgingCompany(
        id: Long,
        command: LodgingCompanyUpdateCommand
    ): LodgingCompanyResponseDto {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        command.update(loadedLodgingCompany)
        return getLodgingCompany(id)
    }

    @CacheEvict(value = ["lodgingCompanyCache"], key = "#id")
    @Transactional
    fun vacation(id: Long) {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        loadedLodgingCompany.vacation()
    }

    @CacheEvict(value = ["lodgingCompanyCache"], key = "#id")
    @Transactional
    fun deleteLodgingCompany(id: Long) {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        loadedLodgingCompany.delete()
    }

}