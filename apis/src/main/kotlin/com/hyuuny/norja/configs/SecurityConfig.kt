package com.hyuuny.norja.configs

import com.hyuuny.norja.filters.JwtFilter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(private val jwtFilter: JwtFilter) : WebSecurityConfigurerAdapter() {


    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers(
                HttpMethod.GET,
                "/",
                "/api/v1/lodging-companies/**",
                "/api/v1/rooms/**",
                "/api/v1/reviews/**",
                "/api/v1/categories/**",
                "/swagger-ui/**",
            )
            .permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/auth", "/api/v1/users/sign-up").permitAll()
            .anyRequest().authenticated()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().mvcMatchers(
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/api-docs/**",
        ).requestMatchers(PathRequest.toStaticResources().atCommonLocations())
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}
