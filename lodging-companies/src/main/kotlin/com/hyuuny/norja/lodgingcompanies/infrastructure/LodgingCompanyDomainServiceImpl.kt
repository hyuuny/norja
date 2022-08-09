package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.DateSearchQuery
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyAndRoomInfo
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyDomainService
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyReader
import com.hyuuny.norja.rooms.domain.RoomReader
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class LodgingCompanyDomainServiceImpl(
    private val lodgingCompanyReader: LodgingCompanyReader,
    private val roomReader: RoomReader,
) : LodgingCompanyDomainService {

    override fun getLodgingCompanyAndRoom(
        id: Long,
        searchQuery: DateSearchQuery
    ): LodgingCompanyAndRoomInfo {
        val parsedCheckIn = LocalDate.parse(searchQuery.checkIn)
        val parsedCheckOut = LocalDate.parse(searchQuery.checkOut)

        val loadedLodgingCompany = lodgingCompanyReader.loadLodgingCompany(id)
        val loadedRooms = roomReader.getRoomsByLodgingCompanyId(
            loadedLodgingCompany.id!!,
            parsedCheckIn,
            parsedCheckOut
        )
        return LodgingCompanyAndRoomInfo(
            loadedLodgingCompany,
            loadedRooms,
            searchQuery.checkIn,
            searchQuery.checkOut
        )
    }
}