package com.hyuuny.norja.rooms

import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.interfaces.RoomResponse
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

@RequestMapping(path = ["/api/v1/rooms"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class RoomRestController(
    private val roomService: RoomService,
    private val roomResourceAssembler: RoomResourceAssembler,
) {


    @GetMapping("/{id}")
    fun getRoom(@PathVariable id: Long): ResponseEntity<EntityModel<RoomResponse>> {
        val loadedRoom = roomService.getRoom(id)
        val resource = RoomResponse(loadedRoom)
        return ResponseEntity.ok(roomResourceAssembler.toModel(resource))
    }

    @Component
    class RoomResourceAssembler :
        RepresentationModelAssembler<RoomResponse, EntityModel<RoomResponse>> {

        override fun toModel(entity: RoomResponse): EntityModel<RoomResponse> {
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