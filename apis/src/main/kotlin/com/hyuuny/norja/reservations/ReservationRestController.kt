package com.hyuuny.norja.reservations

import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.interfaces.ReservationCreateDto
import com.hyuuny.norja.reservations.interfaces.ReservationResponse
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/reservations"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class ReservationRestController(
    private val reservationService: ReservationService,
    private val reservationResourceAssembler: ReservationResourceAssembler,
) {

    @PostMapping
    fun createReservation(@RequestBody dto: ReservationCreateDto): ResponseEntity<Long> {
        val savedReservationId = reservationService.createReservation(dto.toCommand())
        return ResponseEntity.ok(savedReservationId)
    }

    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Long): ResponseEntity<EntityModel<ReservationResponse>> {
        val loadedReservation = reservationService.getReservation(id)
        val resource = ReservationResponse(loadedReservation)
        return ResponseEntity.ok(reservationResourceAssembler.toModel(resource))
    }

    @DeleteMapping("/cancellation/{id}")
    fun requestCancel(@PathVariable id: Long): ResponseEntity<Any> {
        reservationService.requestCancel(id)
        return ResponseEntity.noContent().build()
    }

    @Component
    class ReservationResourceAssembler :
        RepresentationModelAssembler<ReservationResponse, EntityModel<ReservationResponse>> {

        override fun toModel(entity: ReservationResponse): EntityModel<ReservationResponse> {
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