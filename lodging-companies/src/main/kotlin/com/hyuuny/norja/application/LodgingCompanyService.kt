package com.hyuuny.norja.application

import com.hyuuny.norja.domain.LodgingCompanyReader
import com.hyuuny.norja.domain.LodgingCompanyStore
import com.hyuuny.norja.domain.command.LodgingCompanyCreateCommand
import com.hyuuny.norja.domain.command.LodgingCompanyUpdateCommand
import com.hyuuny.norja.domain.info.LodgingCompanyInfo
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