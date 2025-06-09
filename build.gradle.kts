plugins {
    java
    application
    id("io.qameta.allure") version "2.11.2"
}

group = "your.group"
version = "1.0"

application {
    mainClass.set("runner.TestMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.testng:testng:7.9.0")

    testImplementation("org.testng:testng:7.9.0")
    testImplementation("io.qameta.allure:allure-testng:2.24.0")
}

tasks.test {
    useTestNG()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jar {
    archiveBaseName.set("calc-test-framework-fat")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    from(sourceSets["test"].output) // 👈 тестовые классы включаем сюда

    manifest {
        attributes["Main-Class"] = "runner.TestMain"
    }
}