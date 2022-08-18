package com.hyuuny.norja.categories

import com.hyuuny.norja.application.CategoryService
import com.hyuuny.norja.domain.CategoryResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "카테고리 API")
@RequestMapping(path = ["/api/v1/categories"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class CategoryRestController(
    private val categoryService: CategoryService,
    private val categoryResourceAssembler: CategoryResourceAssembler,
) {

    @Operation(summary = "카테고리 전체 조회")
    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryResponse>> {
        val loadedAllCategories = categoryService.getAllCategories()
        return ResponseEntity.ok(loadedAllCategories)
    }

    @Operation(summary = "자녀 카테고리 조회")
    @GetMapping("/{parentCategoryId}/children")
    fun getChildrenCategories(@PathVariable parentCategoryId: Long): ResponseEntity<List<CategoryResponse>> {
        val childrenCategories = categoryService.getChildrenCategories(parentCategoryId)
        return ResponseEntity.ok(childrenCategories)
    }

    @Operation(summary = "카테고리 상세 조회")
    @GetMapping("/{id}")
    fun getCategory(@PathVariable id: Long): ResponseEntity<EntityModel<CategoryResponse>> {
        val loadedCategory = categoryService.getCategory(id)
        return ResponseEntity.ok(categoryResourceAssembler.toModel(loadedCategory))
    }

    @Component
    class CategoryResourceAssembler :
        RepresentationModelAssembler<CategoryResponse, EntityModel<CategoryResponse>> {

        override fun toModel(entity: CategoryResponse): EntityModel<CategoryResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(CategoryRestController::class.java)
                        .getCategory(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}