package com.hyuuny.norja.lodgingcompanies.domain

interface LodgingCompanyStore {

    fun store(lodgingCompany: LodgingCompany): LodgingCompany

}