package com.hyuuny.norja.users.jwts

import com.hyuuny.norja.users.infrastructure.UserDetailsServiceImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtils(private val userDetailsService: UserDetailsServiceImpl) {

    val expiration = 604800

    val secret = "c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK"

    fun createToken(username: String): String {
        val claims: Claims = Jwts.claims();
        claims["type"] = "JWT"
        claims["alg"] = "HS256"
        claims["username"] = username

        return Jwts.builder()
            .setClaims(claims)
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    fun validation(token: String): Boolean {
        val claims: Claims = getAllClaims(token)
        val exp: Date = claims.expiration
        return exp.after(Date())
    }

    fun parseUsername(token: String): String {
        val claims: Claims = getAllClaims(token)
        return claims["username"] as String
    }

    fun getAuthentication(username: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(username)
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }


    // 모든 Claims 조회
    private fun getAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
    }

}