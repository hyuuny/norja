package com.hyuuny.norja

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.CategoryCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.FacilitiesCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.ImageCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyCreateCommand
import com.hyuuny.norja.lodgingcompanies.domain.Type
import com.hyuuny.norja.reviews.domain.ReviewCreateCommand
import com.hyuuny.norja.reviews.domain.ReviewPhotoCreateCommand
import com.hyuuny.norja.rooms.domain.RoomCreateCommand
import com.hyuuny.norja.rooms.domain.RoomFacilitiesCreateCommand
import com.hyuuny.norja.rooms.domain.RoomImageCreateCommand
import com.hyuuny.norja.users.domain.SignUpCommand

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
        fun aLodgingCompanyCommand(categoryId: Long) = LodgingCompanyCreateCommand(
            categoryId,
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
            userId = 1L,
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
            iconImageUrl : String = "icon-image-url",
        ) = CategoryCreateCommand(
            name = name,
            priority = priority,
            level = level,
            iconImageUrl = iconImageUrl,
        )
    }
}