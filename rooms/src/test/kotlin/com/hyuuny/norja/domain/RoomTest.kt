package com.hyuuny.norja.domain

import com.hyuuny.norja.rooms.domain.Room
import com.hyuuny.norja.rooms.domain.RoomFacilities
import com.hyuuny.norja.rooms.domain.RoomImage
import com.hyuuny.norja.rooms.domain.Type
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class RoomTest {

    @Test
    fun `방 등록`() {
        val expectedLodgingCompanyId = 1L
        val expectedType = Type.SINGLE_ROOM
        val expectedName = "일반실"
        val expectedRoomCount = 5
        val expectedStandardPersonnel = 2
        val expectedMaximumPersonnel = 2
        val expectedFacilities = listOf(
            RoomFacilities.create("전용 화장실", "toilet-url", 1L),
            RoomFacilities.create("전용 욕실", "bathroom-url", 2L),
            RoomFacilities.create("TV", "tv-url"),
        )
        val expectedRoomImages = listOf(
            RoomImage.create(1L, "imageUrl-1"),
            RoomImage.create(2L, "imageUrl-2"),
            RoomImage.create(3L, "imageUrl-2"),
        )
        val expectedPrice = 150_000L
        val expectedContent = "넷플릭스 시청가능"

        val newRoom = FixtureRoom.aRoom(roomCount = 5, content = expectedContent)

        newRoom.lodgingCompanyId shouldBe expectedLodgingCompanyId
        newRoom.type shouldBe expectedType
        newRoom.name shouldBe expectedName
        newRoom.roomCount shouldBe expectedRoomCount
        newRoom.standardPersonnel shouldBe expectedStandardPersonnel
        newRoom.maximumPersonnel shouldBe expectedMaximumPersonnel
        newRoom.roomFacilities?.size shouldBe 3
        newRoom.roomFacilities?.get(0)?.name shouldBe expectedFacilities[0].name
        newRoom.roomFacilities?.get(1)?.name shouldBe expectedFacilities[1].name
        newRoom.roomFacilities?.get(2)?.name shouldBe expectedFacilities[2].name
        newRoom.roomImages?.size shouldBe 3
        newRoom.roomImages?.get(0)?.imageUrl shouldBe expectedRoomImages[0].imageUrl
        newRoom.roomImages?.get(1)?.imageUrl shouldBe expectedRoomImages[1].imageUrl
        newRoom.roomImages?.get(2)?.imageUrl shouldBe expectedRoomImages[2].imageUrl
        newRoom.price shouldBe expectedPrice
        newRoom.content shouldBe expectedContent
    }

    @Test
    fun `타입 변경`() {
        val expectedType = Type.DOUBLE_ROOM
        val newRoom = FixtureRoom.aRoom()
        newRoom.changeType(expectedType)
        newRoom.type shouldBe expectedType
    }

    @Test
    fun `이름 변경`() {
        val expectedName = "일반실"
        val newRoom = FixtureRoom.aRoom()
        newRoom.changeName(expectedName)
        newRoom.name shouldBe expectedName
    }

    @Test
    fun `객실 수 변경`() {
        val expectedRoomCount = 20
        val newRoom = FixtureRoom.aRoom()
        newRoom.changeRoomCount(expectedRoomCount)
        newRoom.roomCount shouldBe expectedRoomCount
    }

    @Test
    fun `기준 인원 변경`() {
        val expectedStandardPersonnel = 4
        val newRoom = FixtureRoom.aRoom()
        newRoom.changeStandardPersonnel(expectedStandardPersonnel)
        newRoom.standardPersonnel shouldBe expectedStandardPersonnel
    }

    @Test
    fun `최대 인원 변경`() {
        val expectedMaximumPersonnel = 6
        val newRoom = FixtureRoom.aRoom()
        newRoom.changeMaximumPersonnel(expectedMaximumPersonnel)
        newRoom.maximumPersonnel shouldBe expectedMaximumPersonnel
    }

    @Test
    fun `금액 변경`() {
        val expectedPrice = 320_000L
        val newRoom = FixtureRoom.aRoom()
        newRoom.changePrice(expectedPrice)
        newRoom.price shouldBe expectedPrice
    }

    @Test
    fun `객실 소개 변경`() {
        val expectedContent = "넷플릭스 시청가능"
        val newRoom = FixtureRoom.aRoom()
        newRoom.changeContent(expectedContent)
        newRoom.content shouldBe expectedContent
    }

}

class FixtureRoom {
    companion object {
        fun aRoom(
            lodgingCompanyId: Long = 1L,
            type: Type = Type.SINGLE_ROOM,
            name: String = "일반실",
            roomCount: Int = 1,
            standardPersonnel: Int = 2,
            maximumPersonnel: Int = 2,
            roomFacilities: MutableList<RoomFacilities> = mutableListOf(
                RoomFacilities.create("전용 화장실", "toilet-url"),
                RoomFacilities.create("전용 욕실", "bathroom-url"),
                RoomFacilities.create("TV", "tv-url"),
            ),
            roomImages: MutableList<RoomImage> = mutableListOf(
                RoomImage.create(1L, "imageUrl-1"),
                RoomImage.create(2L, "imageUrl-2"),
                RoomImage.create(3L, "imageUrl-2"),
            ),
            price: Long = 150_000L,
            content: String? = null,
        ) = Room.create(
            lodgingCompanyId,
            type,
            name,
            roomCount,
            standardPersonnel,
            maximumPersonnel,
            roomFacilities,
            roomImages,
            price,
            content,
        )
    }

}