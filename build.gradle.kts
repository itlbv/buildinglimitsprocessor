import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.5.21"
}

group = "com.itlbv"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // Standard Kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Persistence
    implementation("com.vladmihalcea:hibernate-types-52:2.17.1")

    // Simple Features GeoJson
    implementation("mil.nga.sf:sf-geojson:3.1.0")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.12.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
