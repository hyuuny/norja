package com.hyuuny.norja.configs

import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.FixtureAdmin.Companion.anAdmin
import com.hyuuny.norja.FixtureMember.Companion.aMember
import com.hyuuny.norja.MEMBER_EMAIL
import com.hyuuny.norja.users.application.UserService
import com.hyuuny.norja.users.infrastructure.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TestApplicationRunner(
    private val userRepository: UserRepository,
    private val userService: UserService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        userRepository.findByUsername(ADMIN_EMAIL) ?: userService.singUp(
            anAdmin(),
            "ROLE_USER,ROLE_ADMIN"
        )

        userRepository.findByUsername(MEMBER_EMAIL) ?: userService.singUp(
            aMember(),
            "ROLE_USER"
        )
    }

}