import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":tripr-persistence"))
    api(project(":tripr-scraping"))
    api(project(":tripr-security"))

    runtimeOnly("org.postgresql:postgresql")

    api("org.liquibase:liquibase-core")
    api("org.springframework.boot:spring-boot-starter-web")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    api("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.5.0")
    api("org.jetbrains.kotlin:kotlin-reflect")
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("io.rest-assured:rest-assured")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql")
}

configure<SpringBootExtension> {
    mainClass.set("dev.vmillet.tripr.base.TriprApplicationKt")
}

tasks.bootJar {
    enabled = true
}

tasks.jar {
    enabled = true
}
