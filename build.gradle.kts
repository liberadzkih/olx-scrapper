plugins {
    java
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "liberadzkih"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
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
    // lombok
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    //selenium
    implementation("org.seleniumhq.selenium:selenium-java:4.14.1")
    implementation("io.github.bonigarcia:webdrivermanager:5.5.3")

    // jsoup
    implementation("org.jsoup:jsoup:1.8.3")

    // database
    implementation("org.mariadb.jdbc:mariadb-java-client:2.6.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.0")

    // liquibase
    implementation("org.liquibase:liquibase-core")

    // telegram
    implementation("com.github.pengrad:java-telegram-bot-api:6.8.0")

    // spring web to handle controllers
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")


    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
