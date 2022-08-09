package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingInfo
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanySearchQuery
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyListingResponse
import com.hyuuny.norja.lodgingcompanies.interfaces.LodgingCompanyResponse
import com.hyuuny.norja.lodgingcompanies.interfaces.SearchQuery
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.PageImpl
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Tag(name = "숙박업체 API")
@RequestMapping(path = ["/api/v1/lodging-companies"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class LodgingCompanyRestController(
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

    @Operation(summary = "숙박업체 상세 조회")
    @GetMapping("/{id}")
    fun getLodgingCompany(
        @PathVariable id: Long,
        @ParameterObject searchQuery: SearchQuery
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
                        .getLodgingCompany(
                            entity.id,
                            SearchQuery(LocalDate.now().toString(), LocalDate.now().toString())
                        )
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
                            SearchQuery(entity.checkIn.toString(), entity.checkOut.toString())
                        )
                )
                    .withSelfRel()
            )
        }
    }

}