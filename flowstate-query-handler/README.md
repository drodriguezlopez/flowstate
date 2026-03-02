# FlowState Query Handler

Read-side microservice of the **FlowState** task-management system. It handles all query operations on `Task` view models, serving pre-materialised snapshots and audit-log entries to consumers.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Domain Model](#domain-model)
- [API Reference](#api-reference)
- [Configuration](#configuration)
- [Building](#building)
- [Running Locally](#running-locally)
- [Tech Stack](#tech-stack)

---

## Overview

`flowstate-query-handler` is the **CQRS query side** of the FlowState platform. It exposes a REST API (defined by `contracts/openapi-queries.yaml`) that accepts read queries:

| Query                 | Description                                         |
|-----------------------|-----------------------------------------------------|
| List Tasks            | Returns all task snapshots                          |
| Get Task by ID        | Retrieves a single task snapshot by its identifier  |
| Get Task History      | Returns the immutable audit trail for a given task  |

The service reads from its own store (H2 in-memory for local development, MySQL in production) and exposes lightweight, denormalised view models suited for direct client consumption.

---

## Architecture

```
HTTP Client
    │
    ▼
TasksController          (REST layer – delegates to service)
    │
    ▼
TaskService / TaskServiceImpl   (read-only query logic)
    │
    ▼
TaskRepository
    │
    ▼
Read Store (H2 / MySQL)  ◄──  flowstate-event-relay (CDC / projection)
```

API contracts are code-generated at build time from `contracts/openapi-queries.yaml` via the OpenAPI Generator Maven plugin. The controller implements the generated `TasksApi` interface.

---

## Domain Model

### `Task` (view model)

| Field         | Type       | Description                         |
|---------------|------------|-------------------------------------|
| `id`          | `UUID`     | Auto-generated primary key          |
| `title`       | `String`   | Short label for the task            |
| `description` | `String`   | Optional longer description         |
| `status`      | `Status`   | Current lifecycle state             |
| `priority`    | `Priority` | Urgency level                       |

### `Status` enum

| Value         | Meaning              |
|---------------|----------------------|
| `TO_DO`       | Work not yet started |
| `IN_PROGRESS` | Work in progress     |
| `DONE`        | Work completed       |

### `Priority` enum

| Value    | Meaning            |
|----------|--------------------|
| `LOW`    | Low urgency        |
| `MEDIUM` | Normal urgency     |
| `HIGH`   | High urgency       |
| `URGENT` | Critical / blocker |

---

## API Reference

Base path: `/tasks`
Full contract: [`contracts/openapi-queries.yaml`](../contracts/openapi-queries.yaml)

### `GET /tasks` – List All Tasks

Returns a list of all task snapshots.

**Response body**

```json
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "title": "Implement authentication",
    "description": "Configure JWT and user roles",
    "status": "IN_PROGRESS",
    "priority": "HIGH"
  }
]
```

**Responses**

| Code | Description         |
|------|---------------------|
| 200  | List of task summaries |

---

### `GET /tasks/{taskId}` – Get Task by ID

**Path parameter:** `taskId` (UUID)

**Response body**

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "title": "Implement authentication",
  "description": "Configure JWT and user roles",
  "status": "IN_PROGRESS",
  "priority": "HIGH"
}
```

**Responses**

| Code | Description              |
|------|--------------------------|
| 200  | Task snapshot for the given ID |
| 404  | Task not found           |

---

### `GET /tasks/{taskId}/history` – Get Task Audit Log

**Path parameter:** `taskId` (UUID)

Returns the immutable history of all operations performed on a specific task.

**Response body**

```json
[
  {
    "historyId": "1fa11f11-1111-1111-b3fc-1c111f11afa1",
    "taskId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "action": "STATUS_UPDATED",
    "timestamp": "2026-02-27T18:45:00Z",
    "userId": "9ab12c34-5678-90de-f012-3456789abcde"
  }
]
```

**Responses**

| Code | Description                    |
|------|--------------------------------|
| 200  | Audit trail for the given task |
| 404  | Task not found                 |

---

## Configuration

Configuration is driven by `src/main/resources/application-local.yaml` for local development. The most relevant properties can be overridden via environment variables (Spring's relaxed binding):

| Property                        | Default (local)              | Description                    |
|---------------------------------|------------------------------|--------------------------------|
| `server.port`                   | `8081`                       | HTTP port                      |
| `spring.datasource.url`         | `jdbc:h2:mem:testdb`         | JDBC connection URL            |
| `spring.datasource.username`    | `sa`                         | Database username              |
| `spring.datasource.password`    | *(empty)*                    | Database password              |
| `spring.jpa.hibernate.ddl-auto` | `none`                       | Schema generation strategy     |
| `spring.jpa.show-sql`           | `true`                       | Log SQL statements             |
| `spring.h2.console.enabled`     | `true`                       | Enable H2 web console          |
| `spring.h2.console.path`        | `/h2-console`                | H2 console path                |

For production deployments, point `spring.datasource.url` at the shared MySQL read replica and supply the appropriate credentials.

---

## Building

Prerequisites: **Java 21**, **Maven 3.9+**

```bash
# From the repository root
mvn clean package -pl flowstate-query-handler
```

The OpenAPI sources are generated automatically during the `generate-sources` Maven phase from `contracts/openapi-queries.yaml`.

---

## Running Locally

1. No external database is required – the service defaults to an in-memory H2 store.
2. Run the application with the `local` Spring profile:

```bash
mvn spring-boot:run -pl flowstate-query-handler -Dspring-boot.run.profiles=local
```

The service will be available at `http://localhost:8081`.

The H2 web console (for local inspection) is accessible at `http://localhost:8081/h2-console`.

---

## Tech Stack

| Technology        | Version   | Role                                        |
|-------------------|-----------|---------------------------------------------|
| Java              | 21        | Language                                    |
| Spring Boot       | 4.0.3     | Application framework                       |
| Spring Data JPA   | (managed) | ORM / repository abstraction                |
| Hibernate         | (managed) | JPA provider                                |
| H2                | 2.4.240   | In-memory database (local / testing)        |
| MySQL Connector/J | 9.6.0     | Production database driver                  |
| MapStruct         | 1.6.3     | Compile-time bean mapping                   |
| Lombok            | 1.18.32   | Boilerplate reduction                       |
| OpenAPI Generator | 7.20.0    | Contract-first API code generation          |

