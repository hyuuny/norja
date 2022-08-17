package com.hyuuny.norja.users

import com.hyuuny.norja.users.application.AuthService
import com.hyuuny.norja.users.domain.UserWithToken
import com.hyuuny.norja.users.interfaces.CredentialsDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "로그인 API")
@RequestMapping(path = ["/api/v1"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class AuthRestController(
    private val authService: AuthService,
) {
    @Operation(summary = "로그인")
    @PostMapping("/auth")
    fun auth(@RequestBody dto: CredentialsDto): UserWithToken =
        authService.authenticate(dto.toCommand())

}