# 💱 ExchangeRate – NBP JSON Parser

[![version](https://img.shields.io/badge/version-1.1.12-yellow.svg)](https://semver.org)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)
[![Build](https://github.com/rkociniewski/exchange-rate/actions/workflows/main.yml/badge.svg)](https://github.com/rkociniewski/exchange-rate/actions/workflows/main.yml)
[![codecov](https://codecov.io/gh/rkociniewski/exchange-rate/branch/main/graph/badge.svg)](https://codecov.io/gh/rkociniewski/exchange-rate)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blueviolet?logo=kotlin)](https://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-9.10-blue?logo=gradle)](https://gradle.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-greem.svg)](https://opensource.org/licenses/MIT)

A simple Kotlin library for parsing and analyzing currency exchange rates from the **National Bank of Poland (NBP)**
API. It supports both URLs and local JSON files formatted according to NBP’s API.

---

## 🚀 Quick Start

```kotlin
val rate = ExchangeRate("http://api.nbp.pl/api/exchangerates/rates/a/eur/last/10/?format=json")
println("Min: ${rate.getMin()}")
println("Max: ${rate.getMax()}")
````

You can also use a local file:

```kotlin
val rate = ExchangeRate("src/test/resources/exchangeRate.json")
```

---

## 📄 Expected JSON Structure

```json
{
  "rates": [
    { "mid": 4.5371 },
    { "mid": 4.6123 }
  ]
}
```

---

## 📦 Features

* Fetch exchange rate data from URL or file
* Jackson-based JSON deserialization
* Extract minimum and maximum average rates (`mid`)
* Handles edge cases like missing or malformed data

---

## ✅ Tests

Run tests with:

```bash
./gradlew test
```

Test coverage includes:

* Valid data (min/max)
* Empty or malformed JSON
* Missing fields
* Invalid URLs and missing files
* Edge cases like single-element or extreme values

---

## 🔧 Technologies

* Kotlin 2.1.21
* Jackson (Kotlin module)
* JUnit 5

---

## 📁 Project Structure

```
├── src/
│   ├── main/
│   │   └── kotlin/rk/powermilk/ExchangeRate.kt
│   └── test/
│       └── kotlin/ek/powermilk/ExchangeRateTest.kt
```

---

## License

This project is licensed under the MIT License.

## Built With

* [Gradle](https://gradle.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Rafał Kociniewski** - [PowerMilk](https://github.com/rkociniewski)
