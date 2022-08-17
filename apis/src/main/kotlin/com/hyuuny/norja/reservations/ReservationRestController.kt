package com.hyuuny.norja.reservations

import com.hyuuny.norja.reservations.application.ReservationService
import com.hyuuny.norja.reservations.domain.ReservationResponse
import com.hyuuny.norja.reservations.interfaces.ReservationCreateDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.EntityModel
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
) {

    @Operation(summary = "예약 등록")
    @PostMapping
    fun createReservation(@RequestBody dto: ReservationCreateDto): ResponseEntity<Long> {
        val savedReservationId = reservationService.createReservation(dto.toCommand())
        return ResponseEntity.ok(savedReservationId)
    }

    @Operation(summary = "예약 상세 조회")
    @GetMapping("/{id}")
    fun getReservation(@PathVariable id: Long): ResponseEntity<EntityModel<ReservationResponse>> {
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