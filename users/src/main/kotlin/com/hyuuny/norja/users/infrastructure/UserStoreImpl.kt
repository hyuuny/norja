package com.hyuuny.norja.users.infrastructure

import com.hyuuny.norja.users.domain.User
import com.hyuuny.norja.users.domain.UserStore
import org.springframework.stereotype.Component

@Component
class UserStoreImpl(
    private val userRepository: UserRepository,
) : UserStore {

    override fun signUp(user: User) = userRepository.save(user)

}