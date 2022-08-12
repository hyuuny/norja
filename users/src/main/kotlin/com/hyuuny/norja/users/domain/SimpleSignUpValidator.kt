package com.hyuuny.norja.users.domain

import com.hyuuny.norja.users.infrastructure.UserRepository
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class SimpleSignUpValidator(
    private val userRepository: UserRepository
) : SignUpValidator {

    override fun validate(user: User) {
        validateUsername(user.username)
    }

    private fun validateUsername(username: String): Unit? =
        userRepository.findByUsername(username)?.let {
            throw HttpStatusMessageException(HttpStatus.BAD_REQUEST, "user.username.duplicate")
        }
}