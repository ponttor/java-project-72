plugins {
    id("application")
    id("jacoco")
    id("org.sonarqube") version "7.3.0.8198"
    id("checkstyle")
    id("com.github.ben-manes.versions") version "0.53.0"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass = "hexlet.code.App"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:7.2.2")
    implementation("io.javalin:javalin-rendering-jte:7.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("com.zaxxer:HikariCP:7.0.2")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    runtimeOnly("com.h2database:h2:2.4.240")
    runtimeOnly("org.postgresql:postgresql:42.7.11")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sonar {
    properties {
        property("sonar.projectKey", "ponttor_java-project-72")
        property("sonar.organization", "ponttor")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

tasks.test {
    useJUnitPlatform()
}
