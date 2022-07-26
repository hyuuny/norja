package com.hyuuny.norja.lodgingcompanies

import com.hyuuny.norja.lodgingcompanies.application.LodgingCompanyService
import com.hyuuny.norja.lodgingcompanies.domain.DateSearchQuery
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyAndRoomResponseDto
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanyListingResponseDto
import com.hyuuny.norja.lodgingcompanies.domain.LodgingCompanySearchQuery
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
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import javax.validation.constraints.NotNull

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
        pagedResourcesAssembler: PagedResourcesAssembler<LodgingCompanyListingResponseDto>,
    ): ResponseEntity<PagedModel<EntityModel<LodgingCompanyListingResponseDto>>> {
        val page = lodgingCompanyService.retrieveLodgingCompany(searchQuery, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, lodgingCompanyListingResourcesAssembler)
        )
    }

    @Operation(summary = "숙박업체 상세 조회")
    @GetMapping("/{id}")
    fun getLodgingCompany(
        @PathVariable id: Long,
        @ParameterObject @NotNull dateSearchQuery: DateSearchQuery
    ): ResponseEntity<EntityModel<LodgingCompanyAndRoomResponseDto>> {
        val loadedLodgingCompanyAndRooms =
            lodgingCompanyService.getLodgingCompanyAndRooms(id, dateSearchQuery)
        return ResponseEntity.ok(
            lodgingCompanyResourceAssembler.toModel(
                loadedLodgingCompanyAndRooms
            )
        )
    }

    @Component
    class LodgingCompanyListingResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyListingResponseDto, EntityModel<LodgingCompanyListingResponseDto>> {

        override fun toModel(entity: LodgingCompanyListingResponseDto): EntityModel<LodgingCompanyListingResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyRestController::class.java)
                        .getLodgingCompany(
                            entity.id,
                            DateSearchQuery(LocalDate.now().toString(), LocalDate.now().toString())
                        )
                )
                    .withSelfRel()
            )
        }
    }

    @Component
    class LodgingCompanyResourceAssembler :
        RepresentationModelAssembler<LodgingCompanyAndRoomResponseDto, EntityModel<LodgingCompanyAndRoomResponseDto>> {

        override fun toModel(entity: LodgingCompanyAndRoomResponseDto): EntityModel<LodgingCompanyAndRoomResponseDto> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(LodgingCompanyRestController::class.java)
                        .getLodgingCompany(
                            entity.id,
                            DateSearchQuery(entity.checkIn, entity.checkOut)
                        )
                )
                    .withSelfRel()
            )
        }
    }

}