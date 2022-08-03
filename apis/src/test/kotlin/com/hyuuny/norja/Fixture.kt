package com.hyuuny.norja

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.FacilitiesCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.ImageCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.Type
import com.hyuuny.norja.rooms.domain.RoomCreateCommand
import com.hyuuny.norja.rooms.domain.RoomFacilitiesCreateCommand
import com.hyuuny.norja.rooms.domain.RoomImageCreateCommand

class Fixture

class FixtureLodgingCompany {
    companion object {
        fun aLodgingCompanyCommand() = LodgingCompanyCreateCommand(
            type = Type.HOTEL,
            name = "스테이 호텔",
            thumbnail = "thumbnail-url",
            businessNumber = "1231212345",
            tellNumber = "07012341234",
            address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
            searchTag = "스테이, 강남",
            images = mutableListOf(
                ImageCreateCommand(1L, "image1-url"),
                ImageCreateCommand(7L, "image7-url"),
                ImageCreateCommand(5L, "image5-url")
            ),
            facilities = mutableListOf(
                FacilitiesCreateCommand("주차가능", "parking-url", 100L),
                FacilitiesCreateCommand("PC 2대", "parking-url", 300L),
                FacilitiesCreateCommand("넷플릭스 이용 가능", "parking-url", 200L)
            ),
        )
    }
}

class FixtureRoom {
    companion object {
        fun aRoom(lodgingCompanyId: Long) = RoomCreateCommand(
            lodgingCompanyId = lodgingCompanyId,
            type = com.hyuuny.norja.rooms.domain.Type.DOUBLE_ROOM,
            name = "일반실",
            roomCount = 25,
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateCommand(100L, "image1-url"),
                RoomImageCreateCommand(300L, "image3-url"),
                RoomImageCreateCommand(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateCommand("2인", "icon1-url", 100L),
                RoomFacilitiesCreateCommand("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateCommand("정수기 비치", "icon3-url", 200L),
            )
        )
    }
}