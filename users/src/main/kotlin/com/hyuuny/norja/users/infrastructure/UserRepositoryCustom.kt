package com.hyuuny.norja.users.infrastructure

import com.hyuuny.norja.users.domain.SearchedUser
import com.hyuuny.norja.users.domain.UserSearchQuery
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

interface UserRepositoryCustom {

    fun retrieveUser(searchQuery: UserSearchQuery, pageable: Pageable): PageImpl<SearchedUser>

}