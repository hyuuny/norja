package com.hyuuny.norja.reservations

import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.domain.ReservationListingResponse
import com.hyuuny.norja.reservations.domain.ReservationResponse
import com.hyuuny.norja.reservations.domain.ReservationSearchQuery
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "예약 API")
@RequestMapping(path = ["/api/v1/reservations"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ReservationAdminRestController(
    private val reservationService: ReservationService,
    private val reservationListingResourceAssembler: ReservationListingResourceAssembler,
    private val reservationResourceAssembler: ReservationResourceAssembler,
) {

    @Operation(summary = "예약 조회 및 검색")
    @GetMapping
    fun retrieveReservation(
        searchQuery: ReservationSearchQuery,
        @PageableDefault(sort = ["createdAt"], direction = DESC) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<ReservationListingResponse>
    ): ResponseEntity<PagedModel<EntityModel<ReservationListingResponse>>> {
        val page = reservationService.retrieveReservation(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, reservationListingResourceAssembler)
        )
    }

    @Operation(summary = "예약 상세 조회")
    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Long): ResponseEntity<EntityModel<ReservationResponse>> {
        val loadedReservation = reservationService.getReservation(id)
        return ResponseEntity.ok(reservationResourceAssembler.toModel(loadedReservation))
    }

    @Component
    class ReservationListingResourceAssembler :
        RepresentationModelAssembler<ReservationListingResponse, EntityModel<ReservationListingResponse>> {

        override fun toModel(entity: ReservationListingResponse): EntityModel<ReservationListingResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReservationAdminRestController::class.java)
                        .getReservation(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    companion object ReservationResourceAssembler :
        RepresentationModelAssembler<ReservationResponse, EntityModel<ReservationResponse>> {

        override fun toModel(entity: ReservationResponse): EntityModel<ReservationResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(ReservationAdminRestController::class.java)
                        .getReservation(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}
