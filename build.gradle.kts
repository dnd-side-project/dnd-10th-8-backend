plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "${property("projectGroup")}"
version = "${property("applicationVersion")}"

java {
    sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
