package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyCreateDto
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyResponse
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyUpdateDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.hateoas.EntityModel
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
    private val lodgingCompanyResourceAssembler: LodgingCompanyResourceAssembler,
) {

    @Operation(summary = "숙박업체 등록")
    @PostMapping
    fun createLodgingCompany(@RequestBody dto: LodgingCompanyCreateDto): ResponseEntity<Long> {
        val savedLodgingCompanyId = lodgingCompanyService.createLodgingCompany(dto.toCommand())
        return ResponseEntity.ok(savedLodgingCompanyId)
    }

    @Operation(summary = "숙박업체 상세 조회")
    @GetMapping("/{id}")
    fun getLodgingCompany(@PathVariable id: Long): ResponseEntity<EntityModel<LodgingCompanyResponse>> {
        val loadedLodgingCompany = lodgingCompanyService.getLodgingCompany(id)
        val resource = LodgingCompanyResponse(loadedLodgingCompany)
        return ResponseEntity.ok(lodgingCompanyResourceAssembler.toModel(resource))
    }

    @Operation(summary = "숙박업체 수정")
    @PutMapping("/{id}")
    fun updateLodgingCompany(
        @PathVariable id: Long,
        @RequestBody dto: LodgingCompanyUpdateDto
    ): ResponseEntity<EntityModel<LodgingCompanyResponse>> {
        val updatedLodgingCompany = lodgingCompanyService.updateLodgingCompany(id, dto.toCommand())
        val resource = LodgingCompanyResponse(updatedLodgingCompany)
        return ResponseEntity.ok(lodgingCompanyResourceAssembler.toModel(resource))
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
    companion object LodgingCompanyResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyResponse, EntityModel<LodgingCompanyResponse>> {

        override fun toModel(entity: LodgingCompanyResponse): EntityModel<LodgingCompanyResponse> {
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