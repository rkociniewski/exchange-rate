# ExchangeRate

[![version](https://img.shields.io/badge/version-1.1.0-yellow.svg)](https://semver.org)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![License: MIT](https://img.shields.io/badge/License-MIT-greem.svg)](https://opensource.org/licenses/MIT)

The application displays the maximum and minimum value from the exchange rate. By default, the exchange rate from EUR to
GBP from the last 10 days is taken from API of NBP (National Bank of Poland).

## Getting started

First, you need to ensure you have these applications:

- [GIT](https://git-scm.com/) - GIT isn't necessary, but is better to have installed
- IDE (I prefer [Intellij Idea](https://www.jetbrains.com/idea/)) - It isn't necessary, but in IDE you can lookup code
  quicker and nicer.
- [Gradle](https://gradle.org/) - necessary to build project, sometimes IDE have it pre-installed.
- [Java JDK 11](https://www.oracle.com/java/technologies/downloads/#java11) - This one is mandatory ;)

You can download project in two ways:

- By GIT, typing in console this command:

 ```
git clone git@gitlab.com:powermilk-default/exchange-rate.git
 ```

If you decide using this one, I assume you know a basics of GIT

- By download ZIP file. You need to just
  download [this file](https://gitlab.com/powermilk-default/exchange-rate/-/archive/master/exchange-rate-master.zip).

## Prerequisites

You can build this project with [Gradle](https://gradle.org/), so dependencies are automatically downloaded and
imported, but for your information I listed what technologies are used in this repository:

Code:

- [Java 11](https://www.java.com/pl/download/) - If you want to develop this application you will
  need [JDK](https://www.oracle.com/java/technologies/downloads/#java11). Java is programming language what I used to
  write this program.
- [Gson](https://github.com/google/gson) - A serialization or deserialization library to convert POJOs into JSON and
  back from Google.

Testing

- [JUnit 5](https://junit.org/junit5) - The testing Framework.

## Running application

This application just presents solution of some issue, and it can be run itself. It doesn't have `main()` method.

## Running the tests

I used Gradle, so you can run test with this command:

```
gradle test
```

## Built With

* [Gradle](https://gradle.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md)
code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Rafał Kociniewski** - [PowerMilk](https://gitlab.com/rafal.kociniewski)
