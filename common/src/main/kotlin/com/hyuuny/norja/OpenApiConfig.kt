package com.hyuuny.norja

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springdoc.core.SpringDocUtils.getConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.stereotype.Component

@Component
class OpenApiConfig {

    constructor() {
        getConfig().addRequestWrapperToIgnore(PagedResourcesAssembler::class.java)
    }

    @Bean
    fun openAPI(@Value(value = "\${springdoc.version}") appVersion: String): OpenAPI {
        val title = "Norja API"
        val description = "Norja Open API 입니다."
        val termsOfService = "http://localhost:8080"
        val info: Info? = Info().title(title).version(appVersion)
            .description(description)
            .termsOfService(termsOfService)
            .contact(Contact().name("norja").url("").email("shyune@knou.ac.kr"))
            .license(
                License().name("Apach License Version 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")
            )
        return OpenAPI()
            .info(info)
    }

}