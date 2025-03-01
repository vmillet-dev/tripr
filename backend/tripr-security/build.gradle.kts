dependencies {
    api(project(":tripr-persistence"))
    api(project(":tripr-core"))


    api("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-tomcat:3.4.3")
    implementation("com.auth0:java-jwt:4.5.0")
}
