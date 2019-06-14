import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    application
    idea
    kotlin("jvm") version "1.3.31"

    id("org.springframework.boot") version "2.1.5.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"

    // gradle dependencyUpdates -Drevision=release
    id("com.github.ben-manes.versions") version "0.21.0"
    id("com.palantir.docker") version "0.22.1"
}

repositories {
    jcenter()
}

val kotlinLoggingVer = "1.6.26"

val javaxAnnotationApiVer = "1.3.2"
val javaxTransactionApiVer = "1.3"

val testContainersVer = "1.11.3"
val jfairyVer = "0.5.9"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVer")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:testcontainers:$testContainersVer")
    testImplementation("io.codearte.jfairy:jfairy:$jfairyVer")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params")
}

val appName = "app"
val appVer by lazy { "0.0.1+${gitRev()}" }

group = "example"
version = appVer

application {
    mainClassName = "app.AppKt"
    applicationName = appName
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
    buildInfo {
        properties {
            artifact = "$appName-$appVer.jar"
            version = appVer
            name = appName
        }
    }
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
        maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2)
            .takeIf { it > 0 } ?: 1

        testLogging {
            showExceptions = true
            exceptionFormat = TestExceptionFormat.FULL
            showStackTraces = true
            showCauses = true
            showStandardStreams = true
            events = setOf(
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
                TestLogEvent.FAILED
            )
        }

        reports.html.isEnabled = false

        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "5.4.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    bootJar {
        manifest {
            attributes("Multi-Release" to true)
        }

        archiveBaseName.set(appName)
        archiveVersion.set(appVer)

        if (project.hasProperty("archiveName")) {
            archiveFileName.set(project.properties["archiveName"] as String)
        }
    }

    docker {
        val build = build.get()
        val bootJar = bootJar.get()
        val dockerImageName = "${project.group}/$appName"

        dependsOn(build)

        name = "$dockerImageName:latest"
        tag("current", "$dockerImageName:$appVer")
        tag("latest", "$dockerImageName:latest")
        files(bootJar.archiveFile)
        setDockerfile(file("$projectDir/src/main/docker/Dockerfile"))
        buildArgs(
            mapOf(
                "JAR_FILE" to bootJar.archiveFileName.get(),
                "JAVA_OPTS" to "-XX:-TieredCompilation",
                "PORT" to "8080"
            )
        )
        pull(true)
    }

    register("stage") {
        dependsOn("build", "clean")
    }

    register<Delete>("cleanOut") {
        delete("out")
    }

    clean {
        dependsOn("cleanOut")
    }

    withType(DependencyUpdatesTask::class) {
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf(
                        "alpha", "beta", "rc", "cr", "m",
                        "preview", "b", "ea", "eap"
                    ).any { q ->
                        candidate.version.matches(
                            Regex("(?i).*[.-]$q[.\\d-+]*")
                        )
                    }
                    if (rejected) {
                        reject("Release candidate")
                    }
                }
            }
        }

        checkForGradleUpdate = true
        outputFormatter = "json"
        outputDir = "build/dependencyUpdates"
        reportfileName = "report"
    }
}

fun gitRev() = ProcessBuilder("git", "rev-parse", "--short", "HEAD").start().let { p ->
    p.waitFor(100, TimeUnit.MILLISECONDS)
    p.inputStream.bufferedReader().readLine() ?: "none"
}
