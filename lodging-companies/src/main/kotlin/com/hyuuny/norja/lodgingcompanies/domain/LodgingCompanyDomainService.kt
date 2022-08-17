package com.hyuuny.norja.lodgingcompanies.domain

interface LodgingCompanyDomainService {

    fun getLodgingCompanyAndRoom(id: Long, searchQuery: DateSearchQuery): LodgingCompanyAndRoomResponse

}