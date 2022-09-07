package com.hyuuny.norja.categories

import com.hyuuny.norja.application.CategoryService
import com.hyuuny.norja.categories.interfaces.CategoryCreateDto
import com.hyuuny.norja.categories.interfaces.CategoryUpdateDto
import com.hyuuny.norja.domain.CategoryResponseDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@Tag(name = "카테고리 API")
@RequestMapping(path = ["/api/v1/categories"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class CategoryAdminRestController(
    private val categoryService: CategoryService,
    private val categoryResourceAssembler: CategoryResourceAssembler,
) {

    @Operation(summary = "카테고리 등록")
    @PostMapping
    fun createCategory(@RequestBody dto: CategoryCreateDto): ResponseEntity<Long> {
        val savedCategoryId = categoryService.createCategory(dto.toCommand())
        return ResponseEntity.ok(savedCategoryId)
    }

    @Operation(summary = "자녀 카테고리 등록")
    @PostMapping("/{parentCategoryId}/child")
    fun createChildCategory(
        @PathVariable parentCategoryId: Long,
        @RequestBody dto: CategoryCreateDto
    ): ResponseEntity<CategoryResponseDto> {
        val savedChildCategoryId =
            categoryService.createChildCategory(parentCategoryId, dto.toCommand())
        return ResponseEntity.ok(savedChildCategoryId)
    }

    @Operation(summary = "카테고리 전체 조회")
    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryResponseDto>> {
        val loadedAllCategories = categoryService.getAllCategories()
        return ResponseEntity.ok(loadedAllCategories)
    }

    @Operation(summary = "자녀 카테고리 조회")
    @GetMapping("/{parentCategoryId}/children")
    fun getChildrenCategories(@PathVariable parentCategoryId: Long): ResponseEntity<List<CategoryResponseDto>> {
        val childrenCategories = categoryService.getChildrenCategories(parentCategoryId)
        return ResponseEntity.ok(childrenCategories)
    }

    @Operation(summary = "카테고리 상세 조회")
    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): ResponseEntity<EntityModel<CategoryResponseDto>> {
        val loadedCategory = categoryService.getCategory(id)
        return ResponseEntity.ok(categoryResourceAssembler.toModel(loadedCategory))
    }

    @Operation(summary = "카테고리 수정")
    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: Long,
        @RequestBody dto: CategoryUpdateDto
    ): ResponseEntity<EntityModel<CategoryResponseDto>> {
        val updatedCategory = categoryService.updateCategory(id, dto.toCommand())
        return ResponseEntity.ok(categoryResourceAssembler.toModel(updatedCategory))
    }

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Any> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }

    @Component
    companion object CategoryResourceAssembler :
        RepresentationModelAssembler<CategoryResponseDto, EntityModel<CategoryResponseDto>> {

        override fun toModel(entity: CategoryResponseDto): EntityModel<CategoryResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CategoryAdminRestController::class.java)
                        .getCategory(entity.id)
                )
                    .withSelfRel()
            )
        }
    }
}