package com.hyuuny.norja.reviews

import com.hyuuny.norja.reviews.application.ReviewService
import com.hyuuny.norja.reviews.domain.ReviewListingResponseDto
import com.hyuuny.norja.reviews.domain.ReviewResponseDto
import com.hyuuny.norja.reviews.domain.ReviewSearchQuery
import com.hyuuny.norja.reviews.interfaces.ChangeBestReviewDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@Tag(name = "후기 API")
@RequestMapping(path = ["/api/v1/reviews"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ReviewAdminRestController(
    private val reviewService: ReviewService,
    private val reviewResourceAssembler: ReviewResourceAssembler,
    private val reviewListingResourceAssembler: ReviewListingResourceAssembler,
) {

    @Operation(
        summary = "후기 조회 및 검색", description = "정렬: \n"
                + "최신등록순: createdAt,desc(기본)\n"
                + "평점높은순: score,desc\n"
                + "평점낮은순: score,asc"
    )
    @GetMapping
    fun retrieveReview(
        @ParameterObject searchQuery: ReviewSearchQuery,
        @ParameterObject @PageableDefault(
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<ReviewListingResponseDto>
    ): ResponseEntity<PagedModel<EntityModel<ReviewListingResponseDto>>> {
        val page = reviewService.retrieveReview(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, reviewListingResourceAssembler)
        )
    }

    @Operation(summary = "후기 상세 조회")
    @GetMapping("/{id}")
    fun getReview(@PathVariable id: Long): ResponseEntity<EntityModel<ReviewResponseDto>> {
        val loadedReview = reviewService.getReview(id)
        return ResponseEntity.ok(reviewResourceAssembler.toModel(loadedReview))
    }

    @Operation(summary = "후기 삭제")
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: Long): ResponseEntity<Any> {
        reviewService.deleteReview(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "베스트 후기 선정/해제")
    @PostMapping("/best")
    fun changeBestReviews(@RequestBody dto: ChangeBestReviewDto): ResponseEntity<Any> {
        reviewService.changeBestReview(dto.toCommand())
        return ResponseEntity.ok().build()
    }

    @Component
    class ReviewListingResourceAssembler :
        RepresentationModelAssembler<ReviewListingResponseDto, EntityModel<ReviewListingResponseDto>> {

        override fun toModel(entity: ReviewListingResponseDto): EntityModel<ReviewListingResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReviewAdminRestController::class.java)
                        .getReview(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    companion object ReviewResourceAssembler :
        RepresentationModelAssembler<ReviewResponseDto, EntityModel<ReviewResponseDto>> {

        override fun toModel(entity: ReviewResponseDto): EntityModel<ReviewResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReviewAdminRestController::class.java)
                        .getReview(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}