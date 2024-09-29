plugins {
    kotlin("jvm") version "1.8.0"
    id("org.openapi.generator") version "7.8.0"
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("plugin.spring") version "1.8.0"
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
    implementation("io.klogging:klogging-jvm:0.7.2")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:3.1.3")
    implementation("org.springframework.boot:spring-boot-starter-web:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.3.0")
}

tasks.test {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/api/api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("ru.openbook.api")
    invokerPackage.set("ru.openbook.invoker")
    modelPackage.set("ru.openbook.model")

    configOptions.putAll(
        mapOf(
            "dateLibrary" to "java17",
            "useSpringBoot3" to "true",
            "exceptionHandler" to "false",
            "useSwaggerUI" to "false",
            "documentationProvider" to "none",
            "basePackage" to "openBook",
            "apiSuffix" to "Api",
            "skipGeneratePom" to "true",
            "artifactId" to "core-openBook",
            "useBeanValidation" to "false",
            "interfaceOnly" to "true",
            "skipDefaultInterface" to "true",
            "useTags" to "true",
            "serializationLibrary" to "jackson",
        )
    )
    generateApiDocumentation.set(false)
    generateModelDocumentation.set(false)
    generateApiTests.set(false)
}

// Включаем сгенерированный код в компиляцию
sourceSets["main"].java.srcDir("$buildDir/generated/src/main/kotlin")

kotlin {
    jvmToolchain(17)
}

tasks {
    named("build") {
        dependsOn("clean", "openApiGenerate")
    }
    named("openApiGenerate") {
        dependsOn("clean")
    }
    named("compileKotlin") {
        dependsOn("openApiGenerate")
    }
}
