package com.hyuuny.norja.infrastructure

import com.hyuuny.norja.domain.LodgingCompany
import com.hyuuny.norja.domain.LodgingCompanyReader
import com.hyuuny.norja.domain.LodgingCompanyRepository
import com.hyuuny.norja.web.model.HttpStatusMessageException
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

}