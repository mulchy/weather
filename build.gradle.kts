plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.72"

    // annotations
    kotlin("kapt") version "1.3.72"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // junit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")

    // mocking
    testImplementation("io.mockk:mockk:1.10.0")

    // http client
    implementation("com.squareup.okhttp3:okhttp:4.7.2")
    // json deserialization
    implementation("com.squareup.moshi:moshi-kotlin:1.9.3")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.9.3")
}

tasks.test {
    useJUnitPlatform()
}

application {
    // Define the main class for the application.
    mainClassName = "inc.software.acme.AppKt"
}
