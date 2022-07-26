package com.hyuuny.norja.users.domain

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface UserReader {

    fun getUser(id: Long): User

    fun retrieveUser(searchQuery: UserSearchQuery, pageable: Pageable): PageImpl<SearchedUser>

}