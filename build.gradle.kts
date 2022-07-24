import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.10"
    kotlin("kapt") version "1.3.61"
}

allprojects {
    group = "com.hyuuny"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
        runtimeOnly("com.h2database:h2")
        runtimeOnly("mysql:mysql-connector-java")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
        implementation("com.fasterxml.uuid:java-uuid-generator:4.0.1")

        implementation("io.pebbletemplates:pebble-spring-boot-starter:3.1.5")

        implementation("org.springframework.boot:spring-boot-starter-hateoas")

        implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.6.1")
        implementation("org.modelmapper:modelmapper:2.3.7")

        // Open API
        implementation("org.springdoc:springdoc-openapi-ui:1.5.9")
        implementation("org.springdoc:springdoc-openapi-hateoas:1.5.9")
        implementation("org.springdoc:springdoc-openapi-data-rest:1.5.9")

        // RestAssured
        testImplementation("io.rest-assured:rest-assured")

        // kotest
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
        testImplementation("io.kotest:kotest-runner-junit5:5.0.3")
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }

        dependencies {
            dependency("net.logstash.logback:logstash-logback-encoder:6.6")
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

project(":lodging-companies") {
    dependencies {
        implementation(project(":common"))
    }
}

project(":rooms") {
    dependencies {
        implementation(project(":common"))
    }
}

project(":reservation") {
    dependencies {
        implementation(project(":common"))
    }
}

project(":common") {
    val jar: Jar by tasks
    val bootJar: BootJar by tasks

    bootJar.enabled = false
    jar.enabled = true
}