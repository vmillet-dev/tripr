import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    id("org.springframework.boot") version "3.4.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.adarshr.test-logger") version "4.0.0" apply false

    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.allopen") version "1.9.25" apply false
    kotlin("kapt") version "1.9.25" apply false
    kotlin("jvm") version "1.9.25" apply false
}

allprojects {
    group = "dev.vmillet.trpr"
    version = "0.0.1-SNAPSHOT"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
            jvmTarget = "21"
        }
    }

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    apply {
        plugin("java-library")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.kapt")
    }

    dependencies {
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
    }

    // Configure Spring Boot BOM for consistent dependency versions
    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<BootRun> {
        enabled = false
    }

    tasks.withType<BootJar> {
        enabled = false
    }

    tasks.withType<Jar> {
        enabled = true
    }
}
