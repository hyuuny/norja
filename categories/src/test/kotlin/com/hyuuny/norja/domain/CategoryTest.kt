package com.hyuuny.norja.domain

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CategoryTest {

    @Test
    fun `카테고리 등록`() {
        val expectedName = "국내숙소"
        val expectedPriority = 100L
        val expectedLevel = 1
        val expectedIconImageUrl = "icon-image-url"

        val newCategory = FixtureCategory.aCategory()

        newCategory.name shouldBe expectedName
        newCategory.priority shouldBe expectedPriority
        newCategory.level shouldBe expectedLevel
        newCategory.iconImageUrl shouldBe expectedIconImageUrl
    }

    @Test
    fun `이름 변경`() {
        val newCategory = FixtureCategory.aCategory()
        val expectedName = "호캉스"
        newCategory.changeName(expectedName)
        newCategory.name shouldBe expectedName
    }

    @Test
    fun `우선순위 변경`() {
        val newCategory = FixtureCategory.aCategory()
        val expectedPriority = 500L
        newCategory.changePriority(expectedPriority)
        newCategory.priority shouldBe expectedPriority
    }

    @Test
    fun `레벨 변경`() {
        val newCategory = FixtureCategory.aCategory()
        val expectedLevel = 2
        newCategory.changeLevel(expectedLevel)
        newCategory.level shouldBe expectedLevel
    }

    @Test
    fun `아이콘 이미지 URL 변경`() {
        val newCategory = FixtureCategory.aCategory()
        val expectedIconImageUrl = "modify-icon-image-url"
        newCategory.changeIconImageUrl(expectedIconImageUrl)
        newCategory.iconImageUrl shouldBe expectedIconImageUrl
    }

}

class FixtureCategory {
    companion object {
        fun aCategory(
            name: String = "국내숙소",
            priority: Long = 100L,
            level: Int = 1,
            iconImageUrl: String = "icon-image-url",
        ) = Category.create(
            name = name,
            priority = priority,
            level = level,
            iconImageUrl = iconImageUrl,
        )
    }
}