# FlowState Command Handler

> ← Back to [Project README](../README.md)

Write-side microservice of the **FlowState** task-management system. It handles all mutating operations on `Task` aggregates and implements the **Transactional Outbox Pattern** to guarantee reliable event delivery to downstream consumers.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Domain Model](#domain-model)
- [API Reference](#api-reference)
- [Outbox Pattern](#outbox-pattern)
- [Configuration](#configuration)
- [Building](#building)
- [Running Locally](#running-locally)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Tech Stack](#tech-stack)

---

## Overview

`flowstate-command-handler` is the **CQRS command side** of the FlowState platform. It exposes a REST API (defined by `contracts/openapi-commands.yaml`) that accepts write commands:

| Command               | Description                        |
|-----------------------|------------------------------------|
| Create Task           | Creates a new task aggregate       |
| Update Task Status    | Transitions a task through its lifecycle |
| Adjust Task Priority  | Changes the urgency level of a task |

Every write is persisted to MySQL inside a single transaction together with an outbox event record, ensuring atomicity between the state mutation and the event emission.

---

## Architecture

```
HTTP Client
    │
    ▼
TasksController          (REST layer – delegates to service)
    │
    ▼
TaskService / TaskServiceImpl   (business logic + outbox append)
    │              │
    ▼              ▼
TaskRepository  TaskOutboxEventRepository
    │
    ▼
MySQL  ──►  TASK_OUTBOX_EVENT table  ──►  flowstate-event-relay (CDC)
```

API contracts are code-generated at build time from `contracts/openapi-commands.yaml` via the OpenAPI Generator Maven plugin. The controller implements the generated `TasksApi` interface.

---

## Domain Model

### `Task`

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
Full contract: [`contracts/openapi-commands.yaml`](../contracts/openapi-commands.yaml)

### `POST /tasks` – Create a Task

**Request body**

```json
{
  "title": "Implement authentication",
  "description": "Configure JWT and user roles",
  "status": "TO_DO",
  "priority": "HIGH"
}
```

| Field         | Required | Type     |
|---------------|----------|----------|
| `title`       | ✅       | `string` |
| `priority`    | ✅       | `Priority` |
| `description` | ❌       | `string` |
| `status`      | ❌       | `Status` |

**Responses**

| Code | Description              |
|------|--------------------------|
| 201  | Task successfully created |
| 400  | Invalid request body      |

---

### `PATCH /tasks/{taskId}/status` – Update Task Status

**Path parameter:** `taskId` (UUID)

**Request body**

```json
{ "status": "IN_PROGRESS" }
```

**Responses**

| Code | Description               |
|------|---------------------------|
| 204  | Status successfully updated |
| 400  | Invalid status value        |
| 404  | Task not found              |

---

### `PATCH /tasks/{taskId}/priority` – Adjust Task Priority

**Path parameter:** `taskId` (UUID)

**Request body**

```json
{ "priority": "URGENT" }
```

**Responses**

| Code | Description                  |
|------|------------------------------|
| 204  | Priority successfully adjusted |
| 400  | Invalid priority value         |
| 404  | Task not found                 |

---

## Outbox Pattern

Every mutating operation appends a `TaskOutboxEvent` row to the `TASK_OUTBOX_EVENT` table **within the same database transaction** as the `Task` mutation. This guarantees atomicity – either both the state change and the event are committed, or neither is.

### `TASK_OUTBOX_EVENT` table

| Column          | Type        | Description                                         |
|-----------------|-------------|-----------------------------------------------------|
| `ID`            | UUID        | Unique event identifier                             |
| `AGGREGATE_ID`  | UUID        | ID of the `Task` that produced the event            |
| `EVENT_TYPE`    | VARCHAR(128)| Discriminator: `TASK_CREATED`, `TASK_STATUS_UPDATED`, `TASK_PRIORITY_ADJUSTED` |
| `PAYLOAD`       | CLOB/TEXT   | JSON-serialised task snapshot at the time of the operation |
| `CREATED_AT`    | TIMESTAMP   | Wall-clock timestamp of insertion                   |
| `DISPATCHED`    | BOOLEAN     | Set to `true` once the relay has forwarded the event |
| `DISPATCHED_AT` | TIMESTAMP   | Timestamp of successful dispatch (nullable)          |

The `flowstate-event-relay` CDC module polls this table and forwards pending events to the message broker, then marks them as `dispatched`.

---

## Configuration

Configuration is driven by `src/main/resources/application.yaml`. The most relevant properties can be overridden via environment variables (Spring's relaxed binding):

| Property                              | Default                                         | Description              |
|---------------------------------------|-------------------------------------------------|--------------------------|
| `spring.datasource.url`               | `jdbc:mysql://localhost:61392/flowstate`         | JDBC connection URL      |
| `spring.datasource.username`          | `flowuser`                                      | Database username        |
| `spring.datasource.password`          | `flowpass`                                      | Database password        |
| `spring.jpa.hibernate.ddl-auto`       | `update`                                        | Schema generation strategy |
| `spring.jpa.show-sql`                 | `true`                                          | Log SQL statements       |

---

## Building

Prerequisites: **Java 21**, **Maven 3.9+**

```bash
# From the repository root
mvn clean package -pl flowstate-command-handler

# Build and push container image via Jib (requires REGISTRY_URL env var)
mvn jib:build -pl flowstate-command-handler -DREGISTRY_URL=<your-registry>
```

The OpenAPI sources are generated automatically during the `generate-sources` Maven phase.

---

## Running Locally

1. Start a MySQL instance (or use the provided Docker Compose / k8s manifests).
2. Set the datasource properties (override via environment variables or edit `application.yaml`).
3. Run the application:

```bash
mvn spring-boot:run -pl flowstate-command-handler
```

The service will be available at `http://localhost:8080`.

---

## Kubernetes Deployment

A ready-to-use manifest is located at [`k8s/flowstate-command-handler-deployment.yaml`](../k8s/flowstate-command-handler-deployment.yaml). It includes:

- A `Namespace` (`flowstate`)
- A `ConfigMap` with datasource environment variables
- A `Deployment` pulling from `<your-registry>/flowstate-command-handler:latest`
- A `Service` exposing the HTTP port

```bash
kubectl apply -f k8s/flowstate-command-handler-deployment.yaml
```

---

## Tech Stack

| Technology              | Version   | Role                                        |
|-------------------------|-----------|---------------------------------------------|
| Java                    | 21        | Language                                    |
| Spring Boot             | 4.0.3     | Application framework                       |
| Spring Data JPA         | (managed) | ORM / repository abstraction                |
| Hibernate               | (managed) | JPA provider                                |
| MySQL Connector/J       | 9.6.0     | Database driver                             |
| MapStruct               | 1.6.3     | Compile-time bean mapping                   |
| Lombok                  | 1.18.32   | Boilerplate reduction                       |
| OpenAPI Generator       | 7.20.0    | Contract-first API code generation          |
| Jib                     | 3.5.1     | Container image build & push (no Dockerfile)|

