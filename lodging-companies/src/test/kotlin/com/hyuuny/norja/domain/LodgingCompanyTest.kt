package com.hyuuny.norja.domain

import com.hyuuny.norja.address.domain.Address
import com.hyuuny.norja.domain.FixtureLodgingCompany.Companion.aLodgingCompany
import com.hyuuny.norja.lodgingcompanies.domain.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LodgingCompanyTest {

    @Test
    fun `업체 등록`() {
        val expectedCategoryId = 1L
        val expectedType = Type.HOTEL
        val expectedStatus = Status.OPEN
        val expectedName = "스테이 호텔"
        val expectedThumbnail = "thumbnail-url"
        val expectedBusinessNumber = "1231212345"
        val expectedTellNumber = "07012341234"
        val expectedZipcode = "01234"
        val expectedAddress = "서울특별시 강남구 테헤란로 123"
        val expectedDetailAddress = "3층"
        val expectedSearchTag = "스테이, 강남"
        val expectedImages = listOf(
            Image.create(1L, "image-url"),
            Image.create(2L, "image-url")
        )

        val expectedFacilities = listOf(
            Facilities.create("주차가능", "parking-url", 100L),
            Facilities.create("와이파이", "wifi-url", 200L),
            Facilities.create("VOD", "vod-url", 300L)
        )

        val newLodgingCompany = aLodgingCompany()

        newLodgingCompany.categoryId shouldBe expectedCategoryId
        newLodgingCompany.type shouldBe expectedType
        newLodgingCompany.status shouldBe expectedStatus
        newLodgingCompany.name shouldBe expectedName
        newLodgingCompany.thumbnail shouldBe expectedThumbnail
        newLodgingCompany.businessNumber shouldBe expectedBusinessNumber
        newLodgingCompany.tellNumber shouldBe expectedTellNumber
        newLodgingCompany.address.zipcode shouldBe expectedZipcode
        newLodgingCompany.address.address shouldBe expectedAddress
        newLodgingCompany.address.detailAddress shouldBe expectedDetailAddress
        newLodgingCompany.searchTag shouldBe expectedSearchTag
        newLodgingCompany.images?.get(0)?.priority shouldBe expectedImages[0].priority
        newLodgingCompany.images?.get(1)?.priority shouldBe expectedImages[1].priority
        newLodgingCompany.images?.get(0)?.imageUrl shouldBe expectedImages[0].imageUrl
        newLodgingCompany.images?.get(1)?.imageUrl shouldBe expectedImages[1].imageUrl
        newLodgingCompany.facilities?.get(0)?.name shouldBe expectedFacilities[0].name
        newLodgingCompany.facilities?.get(0)?.iconImageUrl shouldBe expectedFacilities[0].iconImageUrl
        newLodgingCompany.facilities?.get(0)?.priority shouldBe expectedFacilities[0].priority
        newLodgingCompany.facilities?.get(1)?.name shouldBe expectedFacilities[1].name
        newLodgingCompany.facilities?.get(1)?.iconImageUrl shouldBe expectedFacilities[1].iconImageUrl
        newLodgingCompany.facilities?.get(1)?.priority shouldBe expectedFacilities[1].priority
        newLodgingCompany.facilities?.get(2)?.name shouldBe expectedFacilities[2].name
        newLodgingCompany.facilities?.get(2)?.iconImageUrl shouldBe expectedFacilities[2].iconImageUrl
        newLodgingCompany.facilities?.get(2)?.priority shouldBe expectedFacilities[2].priority
    }

    @Test
    fun `카테고리 변경`() {
        val expectedCategoryId = 5L
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeCategoryId(expectedCategoryId)
        newLodgingCompany.categoryId shouldBe expectedCategoryId
    }

    @Test
    fun `타입 변경`() {
        val expectedType = Type.POOL_VILLA
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeType(expectedType)
        newLodgingCompany.type shouldBe expectedType
    }

    @Test
    fun `이름 변경`() {
        val expectedName = "속초 바닷길"
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeName(expectedName)
        newLodgingCompany.name shouldBe expectedName
    }

    @Test
    fun `썸네일 변경`() {
        val expectedThumbnail = "update-thumbnail-url"
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeThumbnail(expectedThumbnail)
        newLodgingCompany.thumbnail shouldBe expectedThumbnail
    }

    @Test
    fun `사업자 등록 번호 변경`() {
        val expectedBusinessNumber = "3988892038"
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeBusinessNumber(expectedBusinessNumber)
        newLodgingCompany.businessNumber shouldBe expectedBusinessNumber
    }

    @Test
    fun `전화번호 변경`() {
        val expectedTellNumber = "03212341239"
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeTellNumber(expectedTellNumber)
        newLodgingCompany.tellNumber shouldBe expectedTellNumber
    }

    @Test
    fun `주소 변경`() {
        val expectedAddress = Address("23948", "강원도 속초시 바닷길", "1층")
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeAddress(expectedAddress)
        newLodgingCompany.address.zipcode shouldBe expectedAddress.zipcode
        newLodgingCompany.address.address shouldBe expectedAddress.address
        newLodgingCompany.address.detailAddress shouldBe expectedAddress.detailAddress
    }

    @Test
    fun `검색 태그 변경`() {
        val expectedSearchTag = "속초, 바닷길"
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.changeSearchTag(expectedSearchTag)
        newLodgingCompany.searchTag shouldBe expectedSearchTag
    }

    @Test
    fun `업체 휴가 처리`() {
        val expectedStatus = Status.VACATION
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.vacation()
        newLodgingCompany.status shouldBe expectedStatus
    }

    @Test
    fun `업체 삭제 처리`() {
        val expectedStatus = Status.DELETED
        val newLodgingCompany = aLodgingCompany()
        newLodgingCompany.delete()
        newLodgingCompany.status shouldBe expectedStatus
    }

}

class FixtureLodgingCompany {
    companion object {
        fun aLodgingCompany(
            categoryId: Long = 1L,
            type: Type = Type.HOTEL,
            name: String = "스테이 호텔",
            thumbnail: String = "thumbnail-url",
            businessNumber: String = "1231212345",
            tellNumber: String = "07012341234",
            address: Address = Address("01234", "서울특별시 강남구 테헤란로 123", "3층"),
            searchTag: String = "스테이, 강남",
            images: MutableList<Image> = mutableListOf(
                Image.create(1L, "image-url"),
                Image.create(2L, "image-url")
            ),
            facilities: MutableList<Facilities> = mutableListOf(
                Facilities.create("주차가능", "parking-url", 100L),
                Facilities.create("와이파이", "wifi-url", 200L),
                Facilities.create("VOD", "vod-url", 300L)
            ),
        ) = LodgingCompany.create(
            categoryId,
            type,
            name,
            thumbnail,
            businessNumber,
            tellNumber,
            address,
            searchTag,
            images,
            facilities
        )
    }

}