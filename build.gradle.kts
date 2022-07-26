import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
    kotlin("kapt") version "1.6.21"
}

allprojects {
    group = "com.hyuuny"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    val querydslVersion = "5.0.0"

    apply(plugin = "java")

    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-spring")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "kotlin-kapt")

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
        implementation("com.fasterxml.uuid:java-uuid-generator:4.0.1")

        implementation("org.springframework.boot:spring-boot-starter-hateoas")

        implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.6.1")
        implementation("org.modelmapper:modelmapper:2.3.7")

        implementation("org.springframework.boot:spring-boot-starter-cache")

        // kotest
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
        testImplementation("io.kotest:kotest-runner-junit5:5.0.3")

        // querydsl
        implementation("com.querydsl:querydsl-jpa:$querydslVersion")
        kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
        kapt("org.springframework.boot:spring-boot-configuration-processor")

        // Open API
        implementation("org.springdoc:springdoc-openapi-ui:1.5.9")
        implementation("org.springdoc:springdoc-openapi-hateoas:1.5.9")
        implementation("org.springdoc:springdoc-openapi-data-rest:1.5.9")

        // redis
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        implementation("it.ozimov:embedded-redis:0.7.2")

        // DB
        runtimeOnly("com.h2database:h2")
        runtimeOnly("mysql:mysql-connector-java")

        // Kotlin logging
        implementation("io.github.microutils:kotlin-logging:1.12.5")
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

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
    kotlin.srcDir("$buildDir/generated/source/kapt/main")
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

project(":admin-apis") {
    dependencies {
        // security
        implementation("org.springframework.boot:spring-boot-starter-security")
        testImplementation("org.springframework.security:spring-security-test")

        // jwt
        implementation("io.jsonwebtoken:jjwt:0.9.1")

        // RestAssured
        testImplementation("io.rest-assured:rest-assured")

        implementation(project(":common"))
        implementation(project(":lodging-companies"))
        implementation(project(":rooms"))
        implementation(project(":reservation"))
        implementation(project(":users"))
        implementation(project(":reviews"))
        implementation(project(":categories"))
    }
}

project(":apis") {
    dependencies {
        // security
        implementation("org.springframework.boot:spring-boot-starter-security")
        testImplementation("org.springframework.security:spring-security-test")

        // jwt
        implementation("io.jsonwebtoken:jjwt:0.9.1")

        // RestAssured
        testImplementation("io.rest-assured:rest-assured")

        implementation(project(":common"))
        implementation(project(":lodging-companies"))
        implementation(project(":rooms"))
        implementation(project(":reservation"))
        implementation(project(":users"))
        implementation(project(":reviews"))
        implementation(project(":categories"))
    }
}

project(":lodging-companies") {
    dependencies {
        implementation(project(":common"))
        implementation(project(":rooms"))
        implementation(project(":users"))
        implementation(project(":reviews"))
    }
}

project(":rooms") {
    dependencies {
        implementation(project(":common"))
        implementation(project(":reservation"))
        implementation(project(":users"))
    }
}

project(":reservation") {
    dependencies {
        implementation(project(":common"))
        implementation(project(":users"))
    }
}

project(":users") {
    dependencies {
        // security
        implementation("org.springframework.boot:spring-boot-starter-security")
        testImplementation("org.springframework.security:spring-security-test")

        // jwt
        implementation("io.jsonwebtoken:jjwt:0.9.1")

        implementation(project(":common"))
    }
}

project(":reviews") {
    dependencies {
        implementation(project(":common"))
    }
}

project(":categories") {
    dependencies {
        implementation(project(":common"))
    }
}

tasks.named("bootJar") {
    enabled = false
}

tasks.named("jar") {
    enabled = true
}