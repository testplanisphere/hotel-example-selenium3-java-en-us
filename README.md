# hotel-example-selenium3-java-en-us

[![selenium3-java-en-us](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/actions/workflows/test.yml/badge.svg)](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/actions/workflows/test.yml)

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

#### v2021.2.0 (2021-02-28)

* [#7](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/7) update cache setting
* [#8](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/8) Bump Gradle from 6.6.1 to 6.8.2
* [#9](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/9) remove jcenter
* [#11](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/11) Bump junit-jupiter-api from 5.7.0 to 5.7.1
* [#12](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/12) Bump junit-jupiter-engine from 5.7.0 to 5.7.1
* [#13](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/13) Bump Gradle from 6.8.2 to 6.8.3

#### v2020.9.0 (2020-09-30)

* [#4](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/4) Bump junit-jupiter-api from 5.6.2 to 5.7.0
* [#5](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/5) Bump junit-jupiter-engine from 5.6.2 to 5.7.0

#### v2020.8.0 (2020-08-30)

* [#2](https://github.com/testplanisphere/hotel-example-selenium3-java-en-us/pull/2) Bump Gradle from 6.5.1 to 6.6.1

#### v2020.7.0 (2020-07-26)

* First release
