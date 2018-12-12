import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    idea
    kotlin("jvm") version "1.3.11"

    id("org.springframework.boot") version "2.1.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"

    id("com.gorylenko.gradle-git-properties") version "1.5.2"

    // gradle dependencyUpdates -Drevision=release
    id("com.github.ben-manes.versions") version "0.20.0"
    id("com.palantir.docker") version "0.20.1"
}

repositories {
    jcenter()
}

val kotlinLoggingVer = "1.6.22"

val javaxAnnotationApiVer = "1.3.2"
val javaxTransactionApiVer = "1.3"

val testContainersVer = "1.10.2"
val jfairyVer = "0.5.9"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVer")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:testcontainers:$testContainersVer")
    testImplementation("io.codearte.jfairy:jfairy:$jfairyVer")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClassName = "app.AppKt"
    applicationName = "app"
    version = "0.1-SNAPSHOT"
    group = "example.kotlin.spring"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(JavaVersion.VERSION_11)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

springBoot {
    buildInfo()
}

tasks {
    withType(KotlinCompile::class).configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs = listOf("-progressive")
        }
    }

    withType(JavaCompile::class).configureEach {
        options.isFork = true
    }

    withType(Test::class).configureEach {
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1

        useJUnitPlatform()
        testLogging {
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }

        reports.html.isEnabled = false
        reports.junitXml.isEnabled = false
    }

    wrapper {
        gradleVersion = "5.0"
        distributionType = Wrapper.DistributionType.ALL
    }

    bootJar {
        baseName = "app"

        if (project.hasProperty("archiveName")) {
            archiveName = project.properties["archiveName"] as String
        }
    }

    docker {
        val build = build.get()
        val bootJar = bootJar.get()
        dependsOn(build)
        name = "${project.group}/${bootJar.baseName}"
        files(bootJar.archivePath)
        setDockerfile(file("$projectDir/src/main/docker/Dockerfile"))
        buildArgs(mapOf(
            "JAR_FILE" to bootJar.archiveName,
            "JAVA_OPTS" to "-XX:-TieredCompilation",
            "PORT" to "8080"
        ))
        pull(true)
    }

    register("stage") {
        dependsOn("build", "clean")
    }
}
