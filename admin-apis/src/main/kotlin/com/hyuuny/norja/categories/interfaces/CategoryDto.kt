package com.hyuuny.norja.categories.interfaces

import com.hyuuny.norja.domain.CategoryCreateCommand
import com.hyuuny.norja.domain.CategoryUpdateCommand
import io.swagger.v3.oas.annotations.media.Schema

class CategoryDto

data class CategoryCreateDto(

    @field:Schema(description = "카테고리명", example = "국내숙소", required = true)
    val name: String,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "카테고리 레벨(부모1, 자녀2)", example = "1", required = true)
    val level: Int,

    @field:Schema(description = "아이콘 이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String,
) {
    fun toCommand() = CategoryCreateCommand(
        name = this.name,
        priority = this.priority,
        level = this.level,
        iconImageUrl = this.iconImageUrl
    )
}

data class CategoryUpdateDto(

    @field:Schema(description = "카테고리명", example = "국내숙소", required = true)
    val name: String,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "카테고리 레벨(부모1, 자녀2)", example = "1", required = true)
    val level: Int,

    @field:Schema(description = "아이콘 이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String,
) {
    fun toCommand() = CategoryUpdateCommand(
        name = this.name,
        priority = this.priority,
        level = this.level,
        iconImageUrl = this.iconImageUrl
    )
}
