package com.hyuuny.norja.lodgingcompanies.application

import com.hyuuny.norja.lodgingcompanies.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class LodgingCompanyService(
    private val lodgingCompanyStore: LodgingCompanyStore,
    private val lodgingCompanyReader: LodgingCompanyReader,
) {

    @Transactional
    fun createLodgingCompany(command: LodgingCompanyCreateCommand): Long {
        val newLodgingCompany = command.toEntity
        return lodgingCompanyStore.store(newLodgingCompany).id!!
    }

    fun getLodgingCompany(id: Long): LodgingCompanyInfo {
        val loadedLodgingCompany = lodgingCompanyReader.getLodgingCompany(id)
        return LodgingCompanyInfo(loadedLodgingCompany)
    }

    @Transactional
    fun updateLodgingCompany(id: Long, command: LodgingCompanyUpdateCommand): LodgingCompanyInfo {
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