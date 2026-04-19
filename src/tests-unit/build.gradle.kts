plugins {
    id("java-library")
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

repositories {
    mavenCentral()
}

description = "tests-unit"
group = "skladinya.tests.unit"

dependencies {
    implementation(project(":domain"))
    implementation(project(":persistence-postgres"))
    implementation(project(":tests-helper"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
}

tasks.test {
    useJUnitPlatform()
}

// Add unit tests to the src/test/java in that module to package skladinya.tests.unit.<rest.path.to.tested.class.package>