package com.hyuuny.norja.users.infrastructure

import com.hyuuny.norja.users.domain.SearchedUser
import com.hyuuny.norja.users.domain.User
import com.hyuuny.norja.users.domain.UserReader
import com.hyuuny.norja.users.domain.UserSearchQuery
import com.hyuuny.norja.web.model.HttpStatusMessageException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class UserReaderImpl(
    private val userRepository: UserRepository,
) : UserReader {

    override fun getUser(id: Long): User {
        return userRepository.findByIdOrNull(id)
            ?: throw HttpStatusMessageException(HttpStatus.BAD_REQUEST, "user.id.notFound", id)
    }

    override fun retrieveUser(
        searchQuery: UserSearchQuery,
        pageable: Pageable
    ): PageImpl<SearchedUser> = userRepository.retrieveUser(searchQuery, pageable)

}