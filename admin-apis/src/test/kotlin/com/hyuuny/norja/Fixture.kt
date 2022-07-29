package com.hyuuny.norja

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.lodgingcompanies.domain.Type.HOTEL
import com.hyuuny.norja.lodgingcompanies.interfaces.FacilitiesCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.ImageCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyCreateDto
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.interfaces.RoomCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomFacilitiesCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomImageCreateDto

class Fixture

class FixtureLodgingCompany {
    companion object {
        fun aLodgingCompanyDto() = LodgingCompanyCreateDto(
            type = HOTEL,
            name = "스테이 호텔",
            thumbnail = "thumbnail-url",
            businessNumber = "1231212345",
            tellNumber = "07012341234",
            address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
            searchTag = "스테이, 강남",
            images = mutableListOf(
                ImageCreateDto(100L, "image1-url"),
                ImageCreateDto(700L, "image7-url"),
                ImageCreateDto(500L, "image5-url")
            ),
            facilities = mutableListOf(
                FacilitiesCreateDto("주차가능", "parking-url", 100L),
                FacilitiesCreateDto("PC 2대", "parking-url", 300L),
                FacilitiesCreateDto("넷플릭스 이용 가능", "parking-url", 200L)
            ),
        )
    }
}

class FixtureRoom {
    companion object {
        fun aRoom(lodgingCompanyId: Long) = RoomCreateDto(
            lodgingCompanyId = lodgingCompanyId,
            type = Type.DOUBLE_ROOM,
            name = "일반실",
            standardPersonnel = 2,
            maximumPersonnel = 2,
            price = 130_000,
            content = "코로나 19로 인한 조식 중단",
            images = mutableListOf(
                RoomImageCreateDto(100L, "image1-url"),
                RoomImageCreateDto(300L, "image3-url"),
                RoomImageCreateDto(200L, "image2-url"),
            ),
            facilities = mutableListOf(
                RoomFacilitiesCreateDto("2인", "icon1-url", 100L),
                RoomFacilitiesCreateDto("퀸 침대", "icon2-url", 300L),
                RoomFacilitiesCreateDto("정수기 비치", "icon3-url", 200L),
            )
        )
    }
}