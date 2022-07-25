package com.hyuuny.norja

import org.modelmapper.Conditions
import org.modelmapper.ModelMapper
import org.modelmapper.config.Configuration.AccessLevel
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
class ApplicationConfig {

    @Bean
    fun modelMapper(): ModelMapper {
        val modelMapper = ModelMapper()
        modelMapper.configuration
            .setPropertyCondition(Conditions.isNotNull())
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldAccessLevel(AccessLevel.PRIVATE).isFieldMatchingEnabled = true
        return modelMapper
    }

    @Bean
    fun messageSource(): ResourceBundleMessageSource = ResourceBundleMessageSource().also {
        it.setBasename("i18n/messages")
        it.setDefaultEncoding("UTF-8")
        it.setUseCodeAsDefaultMessage(true)
    }

    @Bean
    fun messageSourceAccessor(): MessageSourceAccessor = MessageSourceAccessor(messageSource())

}