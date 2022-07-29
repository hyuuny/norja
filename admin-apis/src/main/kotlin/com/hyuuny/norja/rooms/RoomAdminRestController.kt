package com.hyuuny.norja.rooms

import com.hyuuny.norja.rooms.application.RoomService
import com.hyuuny.norja.rooms.interfaces.RoomCreateDto
import com.hyuuny.norja.rooms.interfaces.RoomResponse
import com.hyuuny.norja.rooms.interfaces.RoomUpdateDto
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/rooms"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class RoomAdminRestController(
    private val roomService: RoomService,
    private val roomResourceAssembler: RoomResourceAssembler,
) {

    @PostMapping
    fun createRoom(@RequestBody dto: RoomCreateDto): ResponseEntity<Long> {
        val savedRoomId = roomService.createRoom(dto.toCommand())
        return ResponseEntity.ok(savedRoomId)
    }

    @GetMapping("/{id}")
    fun getRoom(@PathVariable id: Long): ResponseEntity<EntityModel<RoomResponse>> {
        val loadedRoom = roomService.getRoom(id)
        val resource = RoomResponse(loadedRoom)
        return ResponseEntity.ok(roomResourceAssembler.toModel(resource))
    }

    @PutMapping("/{id}")
    fun updateRoom(
        @PathVariable id: Long,
        @RequestBody dto: RoomUpdateDto,
    ): ResponseEntity<EntityModel<RoomResponse>> {
        val updatedRoom = roomService.updateRoom(id, dto.toCommand)
        val resource = RoomResponse(updatedRoom)
        return ResponseEntity.ok(roomResourceAssembler.toModel(resource))
    }

    @DeleteMapping("/{id}")
    fun deleteRoom(@PathVariable id: Long): ResponseEntity<Any> {
        roomService.deleteRoom(id)
        return ResponseEntity.noContent().build()
    }

    @Component
    companion object RoomResourceAssembler :
        RepresentationModelAssembler<RoomResponse, EntityModel<RoomResponse>> {

        override fun toModel(entity: RoomResponse): EntityModel<RoomResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(RoomAdminRestController::class.java)
                        .getRoom(entity.id)
                )
                    .withSelfRel()
            )
        }
    }
}