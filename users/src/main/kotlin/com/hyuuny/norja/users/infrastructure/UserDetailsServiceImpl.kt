package com.hyuuny.norja.users.infrastructure

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val loadedUser = (userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("username.notFound $username"))
        return UserAdapter(loadedUser)
    }

}