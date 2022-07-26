package com.hyuuny.norja.users.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonFormat.Shape
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.hateoas.server.core.Relation
import java.time.LocalDateTime

@Relation(collectionRelation = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserListingResponseDto(

    @field:Schema(description = "아이디", example = "1", required = true)
    val id: Long,

    @field:Schema(description = "이메일", example = "hyuuny@knou.ac.kr", required = true)
    val username: String,

    @field:Schema(description = "상태", example = "ACTIVE", required = true)
    val status: Status,

    @field:Schema(description = "닉네임", example = "김성현", required = true)
    val nickname: String,

    @field:Schema(description = "전화번호", example = "010-1234-5678", required = true)
    val phoneNumber: String,

    @field:JsonFormat(
        shape = Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        timezone = "Asia/Seoul"
    )
    @field:Schema(description = "등록일", example = "2022-08-11T21:51:00.797659")
    val createdAt: LocalDateTime,
) {
    companion object {
        operator fun invoke(searched: SearchedUser) = with(searched) {
            UserListingResponseDto(
                id = id!!,
                username = username!!,
                status = status!!,
                nickname = nickname!!,
                phoneNumber = phoneNumber!!,
                createdAt = createdAt!!,
            )
        }
    }
}
