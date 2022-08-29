package com.hyuuny.norja

import com.hyuuny.norja.web.interceptor.LogInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/css/**",
                "/js/**",
                "/fonts/**",
                "/img/**",
                "/images/**",
                "/webfonts/**",
                "/i18n/**",
                "/favicon.ico",
                "/assets/**",
                "/static-bundle/**",
                "/swagger-ui/**",
            )
        registry.addInterceptor(localeChangeInterceptor())
    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()
        localeChangeInterceptor.paramName = "lang"
        return localeChangeInterceptor
    }

}