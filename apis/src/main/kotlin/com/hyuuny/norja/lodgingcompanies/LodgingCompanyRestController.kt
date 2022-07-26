package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyResponse
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

@RequestMapping(path = ["/api/v1/lodging-companies"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class LodgingCompanyRestController(
    private val lodgingCompanyService: LodgingCompanyService,
    private val lodgingCompanyResourceAssembler: LodgingCompanyResourceAssembler,
) {

    @GetMapping("/{id}")
    fun getLodgingCompany(@PathVariable id: Long): ResponseEntity<EntityModel<LodgingCompanyResponse>> {
        val loadedLodgingCompany = lodgingCompanyService.getLodgingCompany(id)
        val resource = LodgingCompanyResponse(loadedLodgingCompany)
        return ResponseEntity.ok(lodgingCompanyResourceAssembler.toModel(resource))
    }

    @Component
    companion object LodgingCompanyResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyResponse, EntityModel<LodgingCompanyResponse>> {

        override fun toModel(entity: LodgingCompanyResponse): EntityModel<LodgingCompanyResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyRestController::class.java)
                        .getLodgingCompany(entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}