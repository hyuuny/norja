package com.hyuuny.norja.reservations

import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.domain.ReservationListingResponseDto
import com.hyuuny.norja.reservations.domain.ReservationResponseDto
import com.hyuuny.norja.reservations.domain.ReservationSearchQuery
import com.hyuuny.norja.reservations.interfaces.ReservationCreateDto
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

@Tag(name = "예약 API")
@RequestMapping(path = ["/api/v1/reservations"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ReservationRestController(
    private val reservationService: ReservationService,
    private val reservationResourceAssembler: ReservationResourceAssembler,
    private val reservationListingResourceAssembler: ReservationListingResourceAssembler,
) {

    @Operation(summary = "예약 조회 및 검색")
    @GetMapping
    fun retrieveReservation(
        @ParameterObject searchQuery: ReservationSearchQuery,
        @ParameterObject @PageableDefault(sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<ReservationListingResponseDto>
    ): ResponseEntity<PagedModel<EntityModel<ReservationListingResponseDto>>> {
        val page = reservationService.retrieveReservation(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, reservationListingResourceAssembler)
        )
    }

    @Operation(summary = "예약 등록")
    @PostMapping
    fun createReservation(@RequestBody dto: ReservationCreateDto): ResponseEntity<Long> {
        val savedReservationId = reservationService.createReservation(dto.toCommand())
        return ResponseEntity.ok(savedReservationId)
    }

    @Operation(summary = "예약 상세 조회")
    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Long): ResponseEntity<EntityModel<ReservationResponseDto>> {
        val loadedReservation = reservationService.getReservation(id)
        return ResponseEntity.ok(reservationResourceAssembler.toModel(loadedReservation))
    }

    @Operation(summary = "예약 취소")
    @DeleteMapping("/cancellation/{id}")
    fun requestCancel(@PathVariable id: Long): ResponseEntity<Any> {
        reservationService.requestCancel(id)
        return ResponseEntity.noContent().build()
    }

    @Component
    class ReservationListingResourceAssembler :
        RepresentationModelAssembler<ReservationListingResponseDto, EntityModel<ReservationListingResponseDto>> {

        override fun toModel(entity: ReservationListingResponseDto): EntityModel<ReservationListingResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReservationRestController::class.java)
                        .getReservation(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    class ReservationResourceAssembler :
        RepresentationModelAssembler<ReservationResponseDto, EntityModel<ReservationResponseDto>> {

        override fun toModel(entity: ReservationResponseDto): EntityModel<ReservationResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReservationRestController::class.java)
                        .getReservation(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}