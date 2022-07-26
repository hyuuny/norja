package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.LodgingCompany
import com.hyuuny.norja.domain.LodgingCompanyRepository
import com.hyuuny.norja.domain.LodgingCompanyStore
import org.springframework.stereotype.Component

@Component
class LodgingCompanyStoreImpl(
    private val lodgingCompanyRepository: LodgingCompanyRepository,
) : LodgingCompanyStore {

    override fun store(lodgingCompany: LodgingCompany) =
        lodgingCompanyRepository.save(lodgingCompany)
}