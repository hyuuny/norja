package com.hyuuny.norja.users

import com.hyuuny.norja.users.application.UserService
import com.hyuuny.norja.users.interfaces.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@Tag(name = "회원 API")
@RequestMapping(path = ["/api/v1/users"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class UserRestController(
    private val userService: UserService,
    private val userResourceAssembler: UserResourceAssembler,
) {

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    fun signup(@RequestBody dto: SignUpDto): ResponseEntity<Long> {
        val savedUserId = userService.singUp(dto.toCommand())
        return ResponseEntity.ok(savedUserId)
    }

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<EntityModel<UserResponse>> {
        val loadedUser = userService.getUser(id)
        val resource = UserResponse(loadedUser)
        return ResponseEntity.ok(userResourceAssembler.toModel(resource))
    }

    @Operation(summary = "비밀번호 변경")
    @PostMapping("/{id}/change-password")
    fun resetPassword(
        @PathVariable id: Long,
        @RequestBody dto: ChangePasswordDto
    ): ResponseEntity<Any> {
        userService.changePassword(id, dto.toCommand())
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "동의 여부 변경")
    @PostMapping("/{id}/change-agreed")
    fun changeAgreed(
        @PathVariable id: Long,
        @RequestBody dto: ChangeAgreedDto
    ): ResponseEntity<Any> {
        userService.changeAgreed(id, dto.toCommand())
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "회원 정보 변경")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody dto: UserUpdateDto
    ): ResponseEntity<EntityModel<UserResponse>> {
        val updatedUser = userService.updateUser(id, dto.toCommand())
        val resource = UserResponse(updatedUser)
        return ResponseEntity.ok(userResourceAssembler.toModel(resource))
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/{id}")
    fun leave(@PathVariable id: Long): ResponseEntity<Any> {
        userService.leave(id)
        return ResponseEntity.noContent().build()
    }


    @Component
    class UserResourceAssembler :
        RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

        override fun toModel(entity: UserResponse): EntityModel<UserResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserRestController::class.java)
                        .getUser(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}