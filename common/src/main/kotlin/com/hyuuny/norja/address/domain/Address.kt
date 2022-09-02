package com.hyuuny.norja.address.domain

import io.swagger.v3.oas.annotations.media.Schema
import javax.persistence.Embeddable

@Embeddable
data class Address(

    @field:Schema(description = "우편번호", example = "12345", required = true)
    var zipcode: String? = null,

    @field:Schema(description = "주소", example = "제주 서귀포시 속골로 29-10", required = true)
    var address: String? = null,

    @field:Schema(description = "상세주소", example = "1층", required = true)
    var detailAddress: String? = null,
)