package com.hyuuny.norja.configs

import com.hyuuny.norja.users.application.UserService
import com.hyuuny.norja.users.infrastructure.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import java.time.Duration

@TestConfiguration
class TestConfig {

    @Bean
    fun restTemplateBuilder(): RestTemplateBuilder? {
        return RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(1))
            .setReadTimeout(Duration.ofSeconds(1))
    }

    @Bean
    fun applicationRunner(
        userRepository: UserRepository,
        userService: UserService,
    ): ApplicationRunner {
        return TestApplicationRunner(userRepository, userService)
    }

}