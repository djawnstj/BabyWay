plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"

    val kotlinVersion = "1.9.25"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

group = "com.djawnstj"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
    annotation("jakarta.persistence.Entity")
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // crypto
    implementation("org.springframework.security:spring-security-crypto")

    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // DB
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Kotlin JDSL
    implementation("com.linecorp.kotlin-jdsl:jpql-dsl:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:jpql-render:3.4.1")
    implementation("com.linecorp.kotlin-jdsl:spring-data-jpa-support:3.4.1")

    // 로깅
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.9")

    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.8.1")

    // 테스트
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // mockk
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    // kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.9.0")
    testImplementation("io.kotest:kotest-assertions-core:5.9.0")
    testImplementation("io.kotest:kotest-framework-datatest:5.9.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers:1.20.2")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation(kotlin("stdlib-jdk8"))

    // Ktor
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")
    implementation("io.ktor:ktor-client-logging:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-client-jackson:2.3.12")
    implementation("io.ktor:ktor-serialization-jackson:2.3.12")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
