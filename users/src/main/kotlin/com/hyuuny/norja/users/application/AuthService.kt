package com.hyuuny.norja.users.application

import com.hyuuny.norja.users.domain.CredentialsCommand
import com.hyuuny.norja.users.domain.Token
import com.hyuuny.norja.users.infrastructure.UserAdapter
import com.hyuuny.norja.users.jwts.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtils: JwtUtils,
) {

    fun auth(command: CredentialsCommand): UserAdapter {
        val usernamePasswordAuthenticationToken =
            UsernamePasswordAuthenticationToken(command.username, command.password)
        val token = authenticationManager.authenticate(usernamePasswordAuthenticationToken)
        return token.principal as UserAdapter
    }

    fun generateToken(username: String): Token {
        val accessToken = jwtUtils.createToken(username)
        return Token(accessToken)
    }

}