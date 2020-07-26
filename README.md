# hotel-example-selenium3-java-en-us

![selenium3-java-en-us](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/workflows/selenium3-java-en-us/badge.svg)

This project is example codes for learning test automation.

### System under test

https://hotel.testplanisphere.dev/en-US/

### Overview

#### Programming Language

* Java

#### Automation Framework

* [Selenium WebDriver](https://www.selenium.dev/)

#### Testing Framework

* [JUnit 5](https://junit.org/junit5/)

#### Build Tool

* [Gradle](https://gradle.org/)

#### Lint Tool

* [Checkstyle](https://checkstyle.sourceforge.io/)

### How to Run

#### Requirements

* JDK 11
* Google Chrome
* [ChromeDriver](https://chromedriver.chromium.org/downloads)

Download Chromedriver and place it in the System `PATH`.

#### Run Tests and lint

##### Windows

```
gradlew.bat clean check
```

##### macOS/Linux

```
./gradlew clean check
```

### Changelog

#### v2020.7.0 (2020-07-26)

* First release
