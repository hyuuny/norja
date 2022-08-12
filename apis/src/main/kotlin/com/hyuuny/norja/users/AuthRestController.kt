package com.hyuuny.norja.users

import com.hyuuny.norja.users.application.AuthService
import com.hyuuny.norja.users.interfaces.CredentialsDto
import com.hyuuny.norja.users.interfaces.UserWithToken
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

    @PostMapping("/auth")
    fun auth(@RequestBody dto: CredentialsDto): UserWithToken {
        val authedUser = authService.auth(dto.toCommand())
        val token = authService.generateToken(authedUser.username)
        return UserWithToken(authedUser, token)
    }

}