import com.github.gradle.node.NodeExtension
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.dsl.SpringBootExtension
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.run.BootRun

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.springframework.boot") version "3.4.3" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.github.node-gradle.node") version "7.1.0" apply false

    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false
    kotlin("plugin.allopen") version "1.9.25" apply false
    kotlin("kapt") version "1.9.25"
}

allprojects {
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
}

subprojects {
    apply {
        plugin("java-library")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.springframework.boot")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    repositories {
        mavenCentral()
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    dependencies {
        val implementation by configurations
        val kapt by configurations
        val developmentOnly by configurations
        val testImplementation by configurations

        implementation("org.mapstruct:mapstruct:1.6.3")
        kapt("org.mapstruct:mapstruct-processor:1.6.3")
        developmentOnly("org.springframework.boot:spring-boot-devtools")
        developmentOnly("org.springframework.boot:spring-boot-docker-compose")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.rest-assured:rest-assured")
        testImplementation("org.springframework.boot:spring-boot-testcontainers")
        testImplementation("org.testcontainers:postgresql")
    }

    extra.apply {
        set("projectName", "${extra.get("rootProjectName")}-$project.name")
    }

    configure<SpringBootExtension> {
        mainClass.set("dev.vmillet.tripr.base.TriprApplicationKt")
    }

    tasks.getByName<BootRun>("bootRun") {
        enabled = false
    }

    tasks.getByName<BootBuildImage>("bootBuildImage") {
        enabled = false
    }

    configurations.create("testArtifacts") {
        extendsFrom(configurations["testImplementation"])
    }

    tasks.register("testJar", Jar::class.java) {
        from(project.the<SourceSetContainer>()["test"].output)
        dependsOn("testClasses")
        archiveClassifier.set("tests")
    }

    artifacts {
        add("testArtifacts", tasks.named<Jar>("testJar") )
    }

    configure<AllOpenExtension> {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.MappedSuperclass")
        annotation("javax.persistence.Embeddable")
    }
}

project(":tripr-web") {
    apply {
        plugin("application")
        plugin("com.github.node-gradle.node")
    }

    tasks.getByName<BootRun>("bootRun") {
        environment.put("SPRING_PROFILES_ACTIVE", environment.get("SPRING_PROFILES_ACTIVE") ?: "local")
        workingDir = rootProject.projectDir
        enabled = true
    }

    tasks.getByName<BootBuildImage>("bootBuildImage") {
        enabled = true
    }

    configure<NodeExtension> {
        download.set(true)
        version.set("22.14.0")
        nodeProjectDir.set(file("${project.projectDir}/.."))
    }

    val npmRunBuild = tasks.register<com.github.gradle.node.npm.task.NpmTask>("npmRunBuild") {
        args.set(listOf("run", "build"))
        dependsOn(tasks.getByName("npmInstall"))

        inputs.files(fileTree("../node_modules"))
        inputs.files(fileTree("../tripr-base/src/main/webapp"))
        inputs.files(fileTree("../tripr-security/src/main/webapp"))
        inputs.files(fileTree("../tripr-scraping/src/main/webapp"))
        inputs.files(fileTree("../tripr-core/src/main/webapp"))
        inputs.files(fileTree("../tripr-web/src/main/webapp"))
        inputs.file("../angular.json")
        inputs.file("../package.json")
        inputs.file("../tsconfig.json")
        inputs.file("../tsconfig.app.json")
        outputs.dir(layout.buildDirectory.dir("resources/main/static"))
    }

    tasks.getByName<ProcessResources>("processResources") {
        dependsOn(npmRunBuild)
    }

    dependencies {
        val api by configurations
        val testImplementation by configurations

        api(project(":tripr-security"))
        api(project(":tripr-scraping"))
        api(project(":tripr-core"))
        testImplementation(project(":tripr-base", "testArtifacts"))
    }
}

project(":tripr-core") {
    dependencies {
        val api by configurations
        val testImplementation by configurations

        api(project(":tripr-base"))
        testImplementation(project(":tripr-base", "testArtifacts"))
    }
}

project(":tripr-scraping") {
    dependencies {
        val api by configurations
        val testImplementation by configurations

        api(project(":tripr-base"))
        testImplementation(project(":tripr-base", "testArtifacts"))
    }
}

project(":tripr-security") {
    dependencies {
        val api by configurations
        val testImplementation by configurations

        api(project(":tripr-base"))
        testImplementation(project(":tripr-base", "testArtifacts"))
    }
}

project(":tripr-base") {
    dependencies {
        val api by configurations
        val runtimeOnly by configurations

        api("org.springframework.boot:spring-boot-starter-web")
        api("org.springframework.boot:spring-boot-starter-validation")
        api("org.jetbrains.kotlin:kotlin-reflect")
        api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        api("org.springframework.boot:spring-boot-starter-security")
        api("com.auth0:java-jwt:4.5.0")
        api("org.springframework.boot:spring-boot-starter-data-jpa")
        runtimeOnly("org.postgresql:postgresql")
        api("org.liquibase:liquibase-core")
        api("org.springframework.boot:spring-boot-starter-actuator")
        api("io.github.wimdeblauwe:error-handling-spring-boot-starter:4.5.0")
        api("org.springframework.boot:spring-boot-starter-thymeleaf")
        api("org.springframework.boot:spring-boot-starter-mail")
        api("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
    }
}
