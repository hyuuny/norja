package com.hyuuny.norja.reservations

import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.domain.ReservationListingInfo
import com.hyuuny.norja.reservations.domain.ReservationSearchQuery
import com.hyuuny.norja.reservations.interfaces.ReservationListingResponse
import com.hyuuny.norja.reservations.interfaces.ReservationResponse
import org.springframework.data.domain.PageImpl
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

@RequestMapping(path = ["/api/v1/reservations"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ReservationAdminRestController(
    private val reservationsService: ReservationService,
    private val reservationListingResourceAssembler: ReservationListingResourceAssembler,
    private val reservationResourceAssembler: ReservationResourceAssembler,
) {

    @GetMapping
    fun retrieveReservation(
        searchQuery: ReservationSearchQuery,
        @PageableDefault(sort = ["createdAt"], direction = DESC) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<ReservationListingResponse>
    ): ResponseEntity<PagedModel<EntityModel<ReservationListingResponse>>> {
        val searched = reservationsService.retrieveReservation(searchQuery, pageable)
        val page = toPage(searched, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, reservationListingResourceAssembler)
        )
    }

    private fun toPage(searched: PageImpl<ReservationListingInfo>, pageable: Pageable) =
        PageImpl(toResponses(searched.content), pageable, searched.totalElements)

    private fun toResponses(searched: List<ReservationListingInfo>) =
        searched.stream().map(::ReservationListingResponse).toList()

    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Long): ResponseEntity<EntityModel<ReservationResponse>> {
        val loadedReservation = reservationsService.getReservation(id)
        val resource = ReservationResponse(loadedReservation)
        return ResponseEntity.ok(reservationResourceAssembler.toModel(resource))
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
