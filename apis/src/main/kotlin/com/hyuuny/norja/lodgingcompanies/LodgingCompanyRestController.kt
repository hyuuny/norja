package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingInfo
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanySearchQuery
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyListingResponse
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyResponse
import com.hyuuny.norja.lodgingcompanies.interfaces.SearchQuery
import org.springframework.data.domain.PageImpl
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
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RequestMapping(path = ["/api/v1/lodging-companies"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class LodgingCompanyRestController(
    private val lodgingCompanyService: LodgingCompanyService,
    private val lodgingCompanyListingResourcesAssembler: LodgingCompanyListingResourceAssembler,
    private val lodgingCompanyResourceAssembler: LodgingCompanyResourceAssembler,
) {

    @GetMapping
    fun retrieveLodgingCompanies(
        searchQuery: LodgingCompanySearchQuery,
        @PageableDefault(sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<LodgingCompanyListingResponse>,
    ): ResponseEntity<PagedModel<EntityModel<LodgingCompanyListingResponse>>> {
        val searched = lodgingCompanyService.retrieveLodgingCompany(searchQuery, pageable)
        val page = toPage(searched, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, lodgingCompanyListingResourcesAssembler)
        )
    }

    private fun toPage(searched: PageImpl<LodgingCompanyListingInfo>, pageable: Pageable) =
        PageImpl(toResponses(searched.content), pageable, searched.totalElements)

    private fun toResponses(searched: List<LodgingCompanyListingInfo>) =
        searched.stream().map(::LodgingCompanyListingResponse).toList()

    @GetMapping("/{id}")
    fun getLodgingCompany(
        @PathVariable id: Long,
        @RequestBody searchQuery: SearchQuery
    ): ResponseEntity<EntityModel<LodgingCompanyResponse>> {
        val loadedLodgingCompanyAndRooms =
            lodgingCompanyService.getLodgingCompanyAndRooms(id, searchQuery.toDateSearchQuery())
        val resource = LodgingCompanyResponse(loadedLodgingCompanyAndRooms)
        return ResponseEntity.ok(lodgingCompanyResourceAssembler.toModel(resource))
    }

    @Component
    class LodgingCompanyListingResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyListingResponse, EntityModel<LodgingCompanyListingResponse>> {

        override fun toModel(entity: LodgingCompanyListingResponse): EntityModel<LodgingCompanyListingResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyRestController::class.java)
                        .getLodgingCompany(entity.id, SearchQuery(LocalDate.now(), LocalDate.now()))
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    class LodgingCompanyResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyResponse, EntityModel<LodgingCompanyResponse>> {

        override fun toModel(entity: LodgingCompanyResponse): EntityModel<LodgingCompanyResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyRestController::class.java)
                        .getLodgingCompany(
                            entity.id,
                            SearchQuery(entity.checkIn, entity.checkOut)
                        )
                )
                    .withSelfRel()
            )
        }
    }

}