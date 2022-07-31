package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompany
import org.springframework.data.jpa.repository.JpaRepository

interface LodgingCompanyRepository : JpaRepository<LodgingCompany, Long>,
    LodgingCompanyRepositoryCustom {
}