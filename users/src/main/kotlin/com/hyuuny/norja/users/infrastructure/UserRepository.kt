package com.hyuuny.norja.users.infrastructure

import com.hyuuny.norja.users.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByUsername(username: String): User?

}