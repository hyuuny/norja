package com.hyuuny.norja.users.domain.collections

import com.hyuuny.norja.users.domain.SearchedUser
import com.hyuuny.norja.users.domain.UserListingResponseDto

data class SearchedUsers(
    val users: List<SearchedUser>,
) {
    fun toPage(): List<UserListingResponseDto> = this.users.stream()
        .map { UserListingResponseDto(it) }
        .toList()
}