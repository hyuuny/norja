package com.hyuuny.norja.rooms

import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.domain.RoomResponseDto
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

@Tag(name = "객실 API")
@RequestMapping(path = ["/api/v1/rooms"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class RoomRestController(
    private val roomService: RoomService,
    private val roomResourceAssembler: RoomResourceAssembler,
) {

    @Operation(summary = "객실 상세 조회")
    @GetMapping("/{id}")
    fun getRoom(@PathVariable id: Long): ResponseEntity<EntityModel<RoomResponseDto>> {
        val loadedRoom = roomService.getRoom(id)
        return ResponseEntity.ok(roomResourceAssembler.toModel(loadedRoom))
    }

    @Component
    class RoomResourceAssembler :
        RepresentationModelAssembler<RoomResponseDto, EntityModel<RoomResponseDto>> {

        override fun toModel(entity: RoomResponseDto): EntityModel<RoomResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(RoomRestController::class.java)
                        .getRoom(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}