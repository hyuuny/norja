package com.hyuuny.norja.lodgingcompanies.domain

interface LodgingCompanyReader {

    fun getLodgingCompany(id: Long): LodgingCompany

}