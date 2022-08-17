package com.hyuuny.norja.users.domain.collections

import com.hyuuny.norja.users.domain.SearchedUser
import com.hyuuny.norja.users.domain.UserListingResponse

data class SearchedUsers(
    val users: List<SearchedUser>,
) {
    fun toPage(): MutableList<UserListingResponse> = this.users.stream()
        .map(::UserListingResponse)
        .toList()
}