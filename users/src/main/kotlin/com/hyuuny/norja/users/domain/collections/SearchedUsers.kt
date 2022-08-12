package com.hyuuny.norja.users.domain.collections

import com.hyuuny.norja.users.domain.SearchedUser
import com.hyuuny.norja.users.domain.UserListingInfo

data class SearchedUsers(
    val users: List<SearchedUser>,
) {
    fun toPage() = this.users.stream()
        .map(::UserListingInfo)
        .toList()
}