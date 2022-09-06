package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingResponseDto
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyResponseDto
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanySearchQuery
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyUpdateDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
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
import org.springframework.web.bind.annotation.*

@Tag(name = "숙박업체 API")
@RequestMapping(path = ["/api/v1/lodging-companies"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class LodgingCompanyAdminRestController(
    private val lodgingCompanyService: LodgingCompanyService,
    private val lodgingCompanyListingResourcesAssembler: LodgingCompanyListingResourceAssembler,
    private val lodgingCompanyResourceAssembler: LodgingCompanyResourceAssembler,
) {

    @Operation(summary = "숙박업체 조회 및 검색")
    @GetMapping
    fun retrieveLodgingCompanies(
        @ParameterObject searchQuery: LodgingCompanySearchQuery,
        @ParameterObject @PageableDefault(
            sort = ["createdAt"],
            direction = DESC
        ) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<LodgingCompanyListingResponseDto>,
    ): ResponseEntity<PagedModel<EntityModel<LodgingCompanyListingResponseDto>>> {
        val page = lodgingCompanyService.retrieveLodgingCompany(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, lodgingCompanyListingResourcesAssembler)
        )
    }

    @Operation(summary = "숙박업체 등록")
    @PostMapping
    fun createLodgingCompany(@RequestBody dto: LodgingCompanyCreateDto): ResponseEntity<Long> {
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())
        return ResponseEntity.ok(savedLodgingCompanyId)
    }

    @Operation(summary = "숙박업체 상세 조회")
    @GetMapping("/{id}")
    fun getLodgingCompany(@PathVariable id: Long): ResponseEntity<EntityModel<LodgingCompanyResponseDto>> {
        val loadedLodgingCompany = lodgingCompanyService.getLodgingCompany(id)
        return ResponseEntity.ok(lodgingCompanyResourceAssembler.toModel(loadedLodgingCompany))
    }

    @Operation(summary = "숙박업체 수정")
    @PutMapping("/{id}")
    fun updateLodgingCompany(
        @PathVariable id: Long,
        @RequestBody dto: LodgingCompanyUpdateDto
    ): ResponseEntity<EntityModel<LodgingCompanyResponseDto>> {
        val updatedLodgingCompany = lodgingCompanyService.updateLodgingCompany(id, dto.toCommand())
        return ResponseEntity.ok(lodgingCompanyResourceAssembler.toModel(updatedLodgingCompany))
    }

    @Operation(summary = "숙박업체 휴무")
    @PutMapping("/vacation/{id}")
    fun vacationLodgingCompany(@PathVariable id: Long): ResponseEntity<Any> {
        lodgingCompanyService.vacation(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "숙박업체 삭제")
    @DeleteMapping("/{id}")
    fun deleteLodgingCompany(@PathVariable id: Long): ResponseEntity<Any> {
        lodgingCompanyService.deleteLodgingCompany(id)
        return ResponseEntity.noContent().build()
    }

    @Component
    class LodgingCompanyListingResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyListingResponseDto, EntityModel<LodgingCompanyListingResponseDto>> {

        override fun toModel(entity: LodgingCompanyListingResponseDto): EntityModel<LodgingCompanyListingResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyAdminRestController::class.java)
                        .getLodgingCompany(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    companion object LodgingCompanyResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyResponseDto, EntityModel<LodgingCompanyResponseDto>> {

        override fun toModel(entity: LodgingCompanyResponseDto): EntityModel<LodgingCompanyResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyAdminRestController::class.java)
                        .getLodgingCompany(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}