package com.hyuuny.norja.users.infrastructure

import com.hyuuny.norja.users.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails

class UserAdapter(private val user: User) : UserDetails {

    private val enabled = true

    override fun getAuthorities(): MutableCollection<GrantedAuthority> =
        AuthorityUtils.createAuthorityList(user.authorities)

    override fun getPassword() = user.password

    override fun getUsername() = user.username

    override fun isAccountNonExpired() = this.enabled

    override fun isAccountNonLocked() = this.enabled

    override fun isCredentialsNonExpired() = this.enabled

    override fun isEnabled() = this.enabled

    fun getId() = user.id!!

    fun getNickname() = user.nickname

    fun getPhoneNumber() = user.phoneNumber

    fun getRoles() = user.roles

    fun getAgreedTermsOfService() = user.agreedTermsOfService

    fun getAgreedPrivacyPolicy() = user.agreedPrivacyPolicy

    fun getAgreedReceiveMessage() = user.agreedReceiveMessage

}