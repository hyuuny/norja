package com.hyuuny.norja.domain

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "category")
@JsonInclude(Include.NON_NULL)
data class CategoryResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "카테고리명", example = "국내숙소", required = true)
    val name: String,

    @field:Schema(description = "우선순위", example = "100", required = true)
    val priority: Long,

    @field:Schema(description = "카테고리 레벨(부모1, 자녀2)", example = "1", required = true)
    val level: Int,

    @field:Schema(description = "아이콘 이미지 URL", example = "icon-image-url", required = true)
    val iconImageUrl: String,

    @field:Schema(description = "부모 카테고리 아이디", example = "1")
    val parentId: Long?,
) {
    companion object {
        operator fun invoke(category: Category) = with(category) {
            CategoryResponseDto(
                id = id!!,
                name = name,
                priority = priority,
                level = level,
                iconImageUrl = iconImageUrl,
                parentId = parentId,
            )
        }
    }
}
