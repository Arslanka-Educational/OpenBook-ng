plugins {
    kotlin("jvm") version "1.8.0"
    id("org.openapi.generator") version "7.8.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib"))
}

tasks.test {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/api/openapi.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("com.example.api")
    invokerPackage.set("com.example.invoker")
    modelPackage.set("com.example.model")
    configOptions.put("dateLibrary", "java8")
}

// Включаем сгенерированный код в компиляцию
sourceSets["main"].java.srcDir("$buildDir/generated/src/main/kotlin")

kotlin {
    jvmToolchain(17)
}