package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyAndRoomInfo
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyDomainService
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyReader
import com.hyuuny.norja.rooms.domain.RoomReader
import org.springframework.stereotype.Component

@Component
class LodgingCompanyDomainServiceImpl(
    private val lodgingCompanyReader: LodgingCompanyReader,
    private val roomReader: RoomReader,
) : LodgingCompanyDomainService {

    override fun getLodgingCompanyAndRoom(id: Long): LodgingCompanyAndRoomInfo {
        val loadedLodgingCompany = lodgingCompanyReader.loadLodgingCompany(id)
        val loadedRooms = roomReader.getRoomsByLodgingCompanyId(loadedLodgingCompany.id!!)
        return LodgingCompanyAndRoomInfo(loadedLodgingCompany, loadedRooms)
    }
}