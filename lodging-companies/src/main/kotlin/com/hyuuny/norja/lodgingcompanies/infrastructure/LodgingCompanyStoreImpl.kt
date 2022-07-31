package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompany
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyStore
import org.springframework.stereotype.Component

@Component
class LodgingCompanyStoreImpl(
    private val lodgingCompanyRepository: LodgingCompanyRepository,
) : LodgingCompanyStore {

    override fun store(lodgingCompany: LodgingCompany) =
        lodgingCompanyRepository.save(lodgingCompany)
}