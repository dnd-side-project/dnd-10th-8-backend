import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.asciidoctor.jvm.convert")
}

group = "${property("projectGroup")}"
version = "${property("applicationVersion")}"

java {
    sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Data
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // Query Builder
    implementation("com.querydsl:querydsl-jpa:${property("queryDslVersion")}:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:${property("queryDslVersion")}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Cloud Infra
    implementation("io.awspring.cloud:spring-cloud-aws-starter:${property("awspringVersion")}")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:${property("awspringVersion")}")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-parameter-store:${property("awspringVersion")}")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:${property("jwtTokenVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jwtTokenVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jwtTokenVersion")}")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("swaggerVersion")}")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Guava
    implementation("com.google.guava:guava:${property("guavaVersion")}-jre")

    // Slack API
    implementation("com.slack.api:slack-api-client:${property("slackApiVersion")}")

    // p6spy
    implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:${property("p6spyVersion")}")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Spring REST Docs (With MockMvc)
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

    // RestAssured (E2E Test)
    testImplementation("io.rest-assured:rest-assured:${property("restAssuredVersion")}")

    // TestContainers
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")

    // TestContainers + Flyway(MySQL)
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:${property("flywayTestExtensionVersion")}")

    // TestContainers + LocalStack
    testImplementation("org.testcontainers:localstack:${property("localStackVersion")}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// QueryDsl QClass
val queryDslTypeDir: String = "src/main/generated"

tasks.withType<JavaCompile>().configureEach {
    options.generatedSourceOutputDirectory = file(queryDslTypeDir)
}

sourceSets {
    getByName("main").java.srcDirs(queryDslTypeDir)
}

tasks.named("clean") {
    doLast {
        file(queryDslTypeDir).deleteRecursively()
    }
}

// build REST Docs
val asciidoctorExt: Configuration by configurations.creating
dependencies {
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}

val snippetsDir: File by extra { file("build/generated-snippets") }
tasks {
    test {
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        configurations(asciidoctorExt.name)
        dependsOn(test)

        doFirst {
            delete(file("src/main/resources/static/docs"))
        }

        inputs.dir(snippetsDir)

        doLast {
            copy {
                from("build/docs/asciidoc")
                into("src/main/resources/static/docs")
            }
        }
    }

    build {
        dependsOn(asciidoctor)
    }
}

// jar & bootJar
tasks.named<Jar>("jar") {
    enabled = false
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName = "Mour"
    archiveFileName = "Mour.jar"
}
