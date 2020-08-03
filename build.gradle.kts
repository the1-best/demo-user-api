import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("io.gitlab.arturbosch.detekt") version "1.8.0"
	id("org.springframework.boot") version "2.2.6.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.71"
	kotlin("plugin.spring") version "1.3.71"
	jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

jacoco {
	toolVersion = "0.8.5"
}

repositories {
	mavenCentral()
	maven { setUrl("https://plugins.gradle.org/m2/") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.springfox:springfox-swagger-ui:2.9.2")
	implementation("io.springfox:springfox-swagger2:2.9.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

configure<DetektExtension> {
	buildUponDefaultConfig = true
	input = files("src/main/kotlin", "src/test/kotlin")
	config = files("detekt.yml")
	reports {
		xml {
			enabled = false
		}
		html {
			enabled = true
		}
	}
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
}

fun Build_gradle.excludeTestFiles(): FileTree {
	return sourceSets.main.get().output.asFileTree.matching {
		exclude("**/exception/*")
		exclude("**/extension/*")
		exclude("**/*ApplicationKt.class")
		exclude("**/*Config*.class")
		exclude("**/*Mock*.class")
	}
}

tasks.jacocoTestReport {
	reports {
		xml.isEnabled = false
		csv.isEnabled = false
		html.destination = file("$buildDir/jacocoHtml")
	}
	classDirectories.setFrom(excludeTestFiles())
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.1".toBigDecimal()
			}
		}
	}
	classDirectories.setFrom(excludeTestFiles())
}

tasks.check {
	finalizedBy(tasks.jacocoTestCoverageVerification)
}

val testCoverage by tasks.registering {
	group = "verification"
	description = "Runs the unit tests with coverage."

	dependsOn(":test", ":jacocoTestReport", ":jacocoTestCoverageVerification")
	val jacocoTestReport = tasks.findByName("jacocoTestReport")
	jacocoTestReport?.mustRunAfter(tasks.findByName("test"))
	tasks.findByName("jacocoTestCoverageVerification")?.mustRunAfter(jacocoTestReport)
}
