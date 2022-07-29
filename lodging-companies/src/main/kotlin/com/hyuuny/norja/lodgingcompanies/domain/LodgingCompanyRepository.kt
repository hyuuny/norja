package com.hyuuny.norja.lodgingcompanies.domain

import org.springframework.data.jpa.repository.JpaRepository

interface LodgingCompanyRepository : JpaRepository<LodgingCompany, Long> {
}