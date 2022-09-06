package com.hyuuny.norja.lodgingcompanies.infrastructure

import com.hyuuny.norja.lodgingcompanies.domain.DateSearchQuery
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyAndRoomResponseDto
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
        dateSearchQuery: DateSearchQuery
    ): LodgingCompanyAndRoomResponseDto {
        val parsedCheckIn = LocalDate.parse(dateSearchQuery.checkIn)
        val parsedCheckOut = LocalDate.parse(dateSearchQuery.checkOut)

        val loadedLodgingCompany = lodgingCompanyReader.loadLodgingCompany(id)
        val loadedRooms = roomReader.getRoomsByLodgingCompanyId(
            loadedLodgingCompany.id!!,
            parsedCheckIn,
            parsedCheckOut
        )
        return LodgingCompanyAndRoomResponseDto(
            loadedLodgingCompany,
            loadedRooms,
            dateSearchQuery.checkIn,
            dateSearchQuery.checkOut
        )
    }
}