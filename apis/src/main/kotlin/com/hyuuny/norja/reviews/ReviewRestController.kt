package com.hyuuny.norja.reviews

import com.hyuuny.norja.reviews.application.ReviewService
import com.hyuuny.norja.reviews.domain.ReviewAverageScoreResponse
import com.hyuuny.norja.reviews.domain.ReviewListingResponse
import com.hyuuny.norja.reviews.domain.ReviewResponse
import com.hyuuny.norja.reviews.domain.ReviewSearchQuery
import com.hyuuny.norja.reviews.interfaces.ReviewCreateDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction.DESC
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
class ReviewRestController(
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
            direction = DESC
        ) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<ReviewListingResponse>
    ): ResponseEntity<PagedModel<EntityModel<ReviewListingResponse>>> {
        val page = reviewService.retrieveReview(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, reviewListingResourceAssembler)
        )
    }

    @Operation(summary = "숙박업체 후기 평균 점수 조회")
    @GetMapping("/lodging-company/{id}")
    fun getAverageScore(@PathVariable("id") lodgingCompanyId: Long): ResponseEntity<ReviewAverageScoreResponse> {
        val loadedAverageScore = reviewService.getAverageScore(lodgingCompanyId)
        return ResponseEntity.ok(loadedAverageScore)
    }

    @Operation(summary = "후기 등록")
    @PostMapping
    fun createReview(@RequestBody dto: ReviewCreateDto): ResponseEntity<Long> {
        val savedReviewId = reviewService.createReview(dto.toCommand())
        return ResponseEntity.ok(savedReviewId)
    }

    @Operation(summary = "후기 상세 조회")
    @GetMapping("/{id}")
    fun getReview(@PathVariable id: Long): ResponseEntity<EntityModel<ReviewResponse>> {
        val loadedReview = reviewService.getReview(id)
        return ResponseEntity.ok(reviewResourceAssembler.toModel(loadedReview))
    }

    @Operation(summary = "후기 삭제")
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id: Long): ResponseEntity<Any> {
        reviewService.deleteReview(id)
        return ResponseEntity.noContent().build()
    }


    @Component
    class ReviewListingResourceAssembler :
        RepresentationModelAssembler<ReviewListingResponse, EntityModel<ReviewListingResponse>> {

        override fun toModel(entity: ReviewListingResponse): EntityModel<ReviewListingResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReviewRestController::class.java)
                        .getReview(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    class ReviewResourceAssembler :
        RepresentationModelAssembler<ReviewResponse, EntityModel<ReviewResponse>> {

        override fun toModel(entity: ReviewResponse): EntityModel<ReviewResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReviewRestController::class.java)
                        .getReview(entity.id)
                )
                    .withSelfRel()
            )
        }
    }
}