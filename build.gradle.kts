plugins {
    java
    checkstyle
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

checkstyle {
    toolVersion = "10.15.0"
    isIgnoreFailures = false
    maxWarnings = 0
    maxErrors = 0
}

val build = "build"
tasks[build].dependsOn("checkstyleMain")
tasks[build].dependsOn("checkstyleTest")

repositories {
    mavenCentral()
}



dependencies {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.hamcrest:hamcrest:2.2")
    implementation("com.rabbitmq:amqp-client:5.20.0")
    implementation("com.evanlennick:retry4j:0.15.0")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("org.mongodb:mongodb-driver-sync:5.1.0")
}

tasks.test {
    useJUnitPlatform()
}


tasks.jar {
    manifest.attributes["Main-Class"] = "org.example.Main"
    val dependencies = configurations
            .runtimeClasspath
            .get()
            .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}