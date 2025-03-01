import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

apply {
    plugin("org.jetbrains.kotlin.plugin.allopen")
}


dependencies {

    api("org.springframework.boot:spring-boot-starter-data-jpa")
}

configure<AllOpenExtension> {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}
