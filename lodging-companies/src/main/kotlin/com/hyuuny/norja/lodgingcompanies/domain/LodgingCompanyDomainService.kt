package com.hyuuny.norja.lodgingcompanies.domain

interface LodgingCompanyDomainService {

    fun getLodgingCompanyAndRoom(id: Long, dateSearchQuery: DateSearchQuery): LodgingCompanyAndRoomResponseDto

}