package com.hyuuny.norja

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

const val ADMIN_APPLICATION_LOCATIONS = "spring.config.location=" +
        "classpath:application.yml"

@EnableJpaAuditing
@SpringBootApplication
class NorjaAdminApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder(NorjaAdminApplication::class.java)
        .properties(ADMIN_APPLICATION_LOCATIONS)
        .run(*args)
}