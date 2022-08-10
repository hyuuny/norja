package com.hyuuny.norja

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class NorjaApplication

const val APPLICATION_LOCATIONS = "spring.config.location=" +
        "classpath:application.yml,"

fun main(args: Array<String>) {
    SpringApplicationBuilder(NorjaApplication::class.java)
        .properties(APPLICATION_LOCATIONS)
        .run(*args)
}