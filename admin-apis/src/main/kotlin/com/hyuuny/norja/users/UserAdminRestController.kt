package com.hyuuny.norja.users

import com.hyuuny.norja.users.application.UserService
import com.hyuuny.norja.users.domain.UserListingResponseDto
import com.hyuuny.norja.users.domain.UserResponseDto
import com.hyuuny.norja.users.domain.UserSearchQuery
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "회원 API")
@RequestMapping(path = ["/api/v1/users"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class UserAdminRestController(
    private val userService: UserService,
    private val userResourceAssembler: UserResourceAssembler,
    private val userListingResourceAssembler: UserListingResourceAssembler,
) {

    @Operation(summary = "회원 조회 및 검색")
    @GetMapping
    fun retrieveUser(
        @ParameterObject searchQuery: UserSearchQuery,
        @ParameterObject @PageableDefault(
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<UserListingResponseDto>,
    ): ResponseEntity<PagedModel<EntityModel<UserListingResponseDto>>> {
        val page = userService.retrieveUser(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, userListingResourceAssembler)
        )
    }

    @Operation(summary = "회원 상세 조회")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<EntityModel<UserResponseDto>> {
        val loadedUser = userService.getUser(id)
        return ResponseEntity.ok(userResourceAssembler.toModel(loadedUser))
    }


    @Component
    class UserListingResourceAssembler :
        RepresentationModelAssembler<UserListingResponseDto, EntityModel<UserListingResponseDto>> {

        override fun toModel(entity: UserListingResponseDto): EntityModel<UserListingResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserAdminRestController::class.java)
                        .getUser(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    class UserResourceAssembler :
        RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {

        override fun toModel(entity: UserResponseDto): EntityModel<UserResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(UserAdminRestController::class.java)
                        .getUser(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}