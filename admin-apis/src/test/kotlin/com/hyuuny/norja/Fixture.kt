package com.hyuuny.norja

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.categories.interfaces.CategoryCreateDto
import com.hyuuny.norja.lodgingcompanies.domain.Type.HOTEL
import com.hyuuny.norja.lodgingcompanies.interfaces.FacilitiesCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.ImageCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyCreateDto
import com.hyuuny.norja.reservations.domain.ReservationCreateCommand
import com.hyuuny.norja.reviews.domain.ReviewCreateCommand
import com.hyuuny.norja.reviews.domain.ReviewPhotoCreateCommand
import com.hyuuny.norja.rooms.domain.Type
import com.hyuuny.norja.rooms.interfaces.RoomCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomFacilitiesCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomImageCreateDto
import com.hyuuny.norja.users.domain.SignUpCommand
import java.time.LocalDate


const val ADMIN_EMAIL = "admin@knou.ac.kr"
const val ADMIN_PASSWORD = "b123456B"

const val MEMBER_EMAIL = "shyune@knou.ac.kr"
const val MEMBER_PASSWORD = "a123456A"

class Fixture

class FixtureAdmin {
    companion object {
        fun anAdmin() = SignUpCommand(
            username = ADMIN_EMAIL,
            password = ADMIN_PASSWORD,
            nickname = "관리자",
            phoneNumber = "010-8462-7121",
        )
    }
}

class FixtureMember {
    companion object {
        fun aMember() = SignUpCommand(
            username = MEMBER_EMAIL,
            password = MEMBER_PASSWORD,
            nickname = "회원",
            phoneNumber = "010-4832-8923",
        )
    }
}

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
            roomCount = 10,
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

class FixtureReservation {
    companion object {
        fun aReservation(roomId: Long, roomCount: Int = 25, price: Long = 130_000) =
            ReservationCreateCommand(
                userId = 1,
                roomId = roomId,
                roomCount = roomCount,
                price = price,
                checkIn = LocalDate.now(),
                checkOut = LocalDate.now().plusDays(5),
            )
    }
}

class FixtureUser {
    companion object {
        fun aUser(username: String = "hyuuny@knou.ac.kr") = SignUpCommand(
            username = username,
            password = "a123456A",
            nickname = "김성현",
            phoneNumber = "010-1234-4567",
        )
    }
}

class FixtureReview {
    companion object {
        fun aReview(
            lodgingCompanyId: Long = 1L,
            roomId: Long = 1L,
            userId: Long = 1L,
            nickname: String = "김성현",
            roomName: String = "더블룸",
            content: String = "방 너무 아늑했어요 ㅋㅋㅋ 다시 오고싶어요!!",
            wholeScore: Int = 5,
            serviceScore: Int = 5,
            cleanlinessScore: Int = 5,
            convenienceScore: Int = 5,
            satisfactionScore: Int = 5,
            reviewPhotos: MutableList<ReviewPhotoCreateCommand> = mutableListOf(
                ReviewPhotoCreateCommand(100L, "image1-url"),
                ReviewPhotoCreateCommand(300L, "image3-url"),
                ReviewPhotoCreateCommand(200L, "image2-url"),
            ),
        ) = ReviewCreateCommand(
            lodgingCompanyId = lodgingCompanyId,
            roomId = roomId,
            userId = userId,
            nickname = nickname,
            roomName = roomName,
            content = content,
            wholeScore = wholeScore,
            serviceScore = serviceScore,
            cleanlinessScore = cleanlinessScore,
            convenienceScore = convenienceScore,
            satisfactionScore = satisfactionScore,
            reviewPhotos = reviewPhotos,
        )
    }
}

class FixtureCategory {
    companion object {
        fun aCategory(
            name: String = "국내호텔",
            priority: Long = 100,
            level: Int = 1,
            iconImageUrl: String = "icon-image-url",
        ) = CategoryCreateDto(
            name = name,
            priority = priority,
            level = level,
            iconImageUrl = iconImageUrl,
        )
    }
}