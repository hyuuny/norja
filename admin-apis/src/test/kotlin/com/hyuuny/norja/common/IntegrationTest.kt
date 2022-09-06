package com.hyuuny.norja.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.hyuuny.norja.ADMIN_EMAIL
import com.hyuuny.norja.MEMBER_EMAIL
import com.hyuuny.norja.redis.RedisRepository
import com.hyuuny.norja.users.AUTH_REQUEST_URL
import com.hyuuny.norja.users.application.UserService
import com.hyuuny.norja.users.domain.UserWithToken
import com.hyuuny.norja.users.infrastructure.UserRepository
import com.hyuuny.norja.users.interfaces.CredentialsDto
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ExtendWith(SpringExtension::class)
@MockMvcCustomConfig
@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    properties = ["spring.config.location="
            + "classpath:application.yml,"
            + "classpath:application-test.yml"]
)
abstract class BaseIntegrationTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var redisRepository: RedisRepository

    @Throws(Exception::class)
    protected fun getBearerToken(username: String, password: String) =
        getAccessToken(username, password)

    @Throws(Exception::class)
    private fun getAccessToken(username: String, password: String): String {
        userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
        val perform = mockMvc.perform(
            post("$AUTH_REQUEST_URL/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CredentialsDto(username, password)))
        )
        val responseBody = perform.andReturn().response.contentAsString
        return objectMapper.readValue(responseBody, UserWithToken::class.java).token.accessToke
    }

    fun deleteAllUsers() {
        userRepository.findAll().stream()
            .filter { user -> user.username != ADMIN_EMAIL }
            .filter { user -> user.username != MEMBER_EMAIL }
            .forEach { user -> userRepository.delete(user) }
    }

    fun deleteCache() {
        this.redisRepository.clear()
    }

}