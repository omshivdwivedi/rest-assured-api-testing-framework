# REST Assured API Automation Framework

A REST API test automation framework built with **REST Assured** and **TestNG** for testing the
[Restful Booker API](https://restful-booker.herokuapp.com/), a public demo hotel-booking service.

The framework follows a clean, layered design (service objects + POJOs + tests + utilities), supports
data-driven testing from CSV/Excel, runs tests in parallel, and produces rich HTML reports via
ExtentReports.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Tests](#running-the-tests)
- [Configuration](#configuration)
- [Test Data](#test-data)
- [Reports](#reports)
- [Test Coverage](#test-coverage)


---

## Tech Stack

| Concern                     | Technology             | Version   |
| --------------------------- | ---------------------- | --------- |
| Language                    | Java                   | 17        |
| Build tool                  | Maven                  | —         |
| API testing                 | REST Assured           | 5.5.7     |
| Test runner                 | TestNG                 | 7.11.0    |
| JSON (de)serialization      | Jackson Databind       | 2.22.0    |
| CSV data-driven testing     | OpenCSV                | 5.12.0    |
| Excel data-driven testing   | Apache POI (poi-ooxml) | 5.5.1     |
| Reporting                   | ExtentReports (Spark)  | 5.1.2     |
| Test execution plugin       | Maven Surefire         | 3.5.6     |

---

## Project Structure

```
rest-assured-api-testing-framework/
├── pom.xml                         # Maven build & dependencies
├── testng.xml                      # TestNG suite definition (parallel by classes)
├── README.md
└── src/
    └── test/
        ├── java/
        │   └── api/
        │       ├── endpoints/      # Service objects (one per API resource)
        │       │   ├── AuthAPI.java
        │       │   ├── PingAPI.java
        │       │   └── BookingAPI.java
        │       ├── payloads/       # Request/response POJOs (Jackson)
        │       │   ├── AuthReqPayload.java
        │       │   ├── AuthResPayload.java
        │       │   ├── BookingDates.java
        │       │   ├── BookingReqPayload.java
        │       │   └── BookingResPayload.java
        │       ├── tests/          # TestNG test classes
        │       │   ├── BaseTest.java            # Base class: attaches ExtentReports listener
        │       │   ├── HealthCheckTests.java
        │       │   ├── AuthTests.java
        │       │   ├── BookingTests.java
        │       │   └── BookingDataDrivenTest.java
        │       └── utils/          # Reusable helpers
        │           ├── PropertyReader.java
        │           ├── DataReaderCSV.java
        │           ├── DataReader.java          # Excel reader (Apache POI)
        │           └── ExtentReportManager.java # TestNG listener + reporting
        └── resources/
            ├── config.properties               # Base URL, credentials, report config
            ├── schema/
            │   └── getBookingSchema.json        # JSON Schema for booking response validation
            └── testdata/
                └── bookingData.csv              # Data-driven test input
```

---
## Architecture

The framework is organized into distinct, single-responsibility layers:

- **Endpoints (Service Objects)** — Each class wraps a single API resource and exposes methods that
  build and execute requests using REST Assured. Every call is logged to the Extent report via
  `ExtentReportManager.logApiDetails(...)`.

- **Payloads (POJOs)** — Plain Java objects mapped to/from JSON by Jackson. Tests build strongly-typed
  request objects and deserialize responses with `response.as(SomePayload.class)` instead of dealing
  with raw JSON strings.

- **Tests** — TestNG classes containing the assertions. `BookingTests` demonstrates an end-to-end CRUD
  flow using `priority` and `dependsOnMethods` to sequence dependent operations.

- **Utils** — Cross-cutting helpers:
  - `PropertyReader` loads `config.properties` once (static initializer).
  - `DataReaderCSV` / `DataReader` provide data-driven inputs from CSV and Excel respectively.
  - `ExtentReportManager` implements `ITestListener` and uses a `ThreadLocal<ExtentTest>` for
    thread-safe reporting during parallel execution.

---

## Prerequisites

- **JDK 17** or later (the project targets Java 17 — see `pom.xml`)
- **Apache Maven 3.9+**
- Internet access (tests run against the live public Restful Booker API)


Verify your setup:

```powershell
java -version
mvn -version
```

---

## Getting Started

Clone the repository and install dependencies:

```powershell
git clone <repository-url>
cd rest-assured-api-testing-framework
mvn clean install -DskipTests
```

---

## Running the Tests

Run the full suite (defined in `testng.xml`) via Surefire:

```powershell
mvn clean test
```

Run a single test class:

```powershell
mvn test "-Dtest=BookingTests"
```

Run a single test method:

```powershell
mvn test "-Dtest=BookingTests#createBooking"
```

> **Note:** Tests execute in parallel by class (`thread-count=4`), as configured in `testng.xml`.

---

## Configuration

All environment and reporting settings live in `src/test/resources/config.properties`:

| Property         | Description                              | Example                                    |
| ---------------- | ---------------------------------------- | ------------------------------------------ |
| `baseurl`        | Base URL of the API under test           | `https://restful-booker.herokuapp.com/`    |
| `auth_username`  | Username for token generation            | `admin`                                    |
| `auth_password`  | Password for token generation            | `password123`                              |
| `report.path`    | Output directory for HTML reports        | `reports`                                  |
| `report.name`    | Report name shown in ExtentReports       | `Rest Assured API Automation`              |
| `report.title`   | Document title of the report             | `Automation Execution Report`              |
| `report.theme`   | Report theme (`DARK` or `STANDARD`)      | `DARK`                                      |
| `application`    | Application name (report system info)    | `Restful Booker API`                       |
| `environment`    | Environment label (report system info)   | `QA`                                       |

To point the suite at a different environment, update `baseurl` (and credentials if needed).

---

## Test Data

Data-driven tests read from `src/test/resources/testdata/bookingData.csv`. The header row maps
directly to booking fields:

```csv
firstname,lastname,totalprice,depositpaid,checkin,checkout,additionalneeds
Dennis,Ritchie,500,true,2026-07-10,2026-07-15,Breakfast
Peter,Parker,750,false,2026-07-12,2026-07-20,Lunch
...
```

Add rows to the CSV to expand data-driven coverage — no code changes required. The framework also
includes `DataReader` (Apache POI) for reading test data from Excel `.xlsx` files if needed.

---

## Reports

After each run, an ExtentReports HTML report is generated in the configured `report.path` directory
with a timestamped filename:

```
reports/API_Report_<yyyy.MM.dd.HH.mm.ss>.html
```

Each report includes per-test request/response details (endpoint, HTTP method, request body, status
code, response time, headers, and response body), plus system info (OS, Java version, environment).

---

## Test Coverage

| Suite / Class            | What it validates                                                        |
| ------------------------ | ------------------------------------------------------------------------ |
| `HealthCheckTests`       | `GET /ping` health endpoint returns `201 Created`                        |
| `AuthTests`              | `POST /auth` returns `200` and a non-empty token                         |
| `BookingTests`           | End-to-end CRUD flow: create, get, get-all, filter by name/date, update, partial update, delete, and verify deletion |
| `BookingDataDrivenTest`  | Creates bookings from multiple CSV data rows and verifies the response    |

---





