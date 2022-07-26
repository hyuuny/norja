package com.hyuuny.norja.common

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder
import org.springframework.web.filter.CharacterEncodingFilter

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@AutoConfigureMockMvc
@Import(*[MockMvcCustomConfig.Config::class])
annotation class MockMvcCustomConfig {
    class Config {

        @Bean
        fun utf8CharacterEncodingFilter(): MockMvcBuilderCustomizer {
            return MockMvcBuilderCustomizer { builder: ConfigurableMockMvcBuilder<*> ->
                builder.addFilters(CharacterEncodingFilter("UTF-8", true))
            }
        }
    }
}