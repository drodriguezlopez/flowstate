# FlowState Architecture & Technical Overview

## Introduction
FlowState is a modular, CQRS-based Task Management System designed for scalability, auditability, and clear separation of concerns. This document provides an overview of the architecture and technical details, referencing the domain model and module-level diagrams.

---

## Architectural Overview

FlowState is structured around the Command Query Responsibility Segregation (CQRS) pattern, separating the write (command) and read (query) sides for optimal performance and maintainability.

### Key Modules
- **Presentation Layer**: Mobile/Web client interfaces for user interaction.
- **Command Service (Write Side)**: Handles all state-changing operations, including task creation, updates, and event journaling.
- **Query Service (Read Side)**: Optimized for fast, scalable data retrieval and dashboard views.
- **Message Backbone**: Event bus for asynchronous communication and synchronization between modules.

### Data Stores
- **Primary DB**: Stores authoritative task state and outbox journal for event sourcing.
- **Read Store**: Maintains denormalized, query-optimized views for fast access.

---

## Domain Model

The core domain entities are:
- **Task (Aggregate Root)**: Central entity enforcing business rules and state transitions.
- **TaskSummary (Read Model/DTO)**: Lightweight projection for fast display and dashboard queries.
- **TaskHistory (Event/Audit Log)**: Tracks all changes for audit and timeline views.

Enums:
- **Status**: TO_DO, IN_PROGRESS, DONE
- **Priority**: LOW, MEDIUM, HIGH, URGENT

---

## Module-Level Flow

1. **User Action**: Client submits a command (e.g., create or update task).
2. **Command Gateway**: Routes command to appropriate handler.
3. **Task Handler**: Updates entity state and appends event to outbox journal.
4. **Event Relay**: Reads journal and dispatches events to the message bus.
5. **Message Bus**: Synchronizes read models in the query service.
6. **Query Gateway**: Handles dashboard and list queries, fetching optimized data from the read store.

---

## Technical Highlights
- **CQRS & Event Sourcing**: Ensures separation of write/read concerns and full auditability.
- **Outbox Pattern**: Guarantees reliable event delivery and eventual consistency.
- **Modular Design**: Each module is independently deployable and scalable.
- **Technology Stack**: Java (Spring Boot), SQL/NoSQL databases, message broker (e.g., Kafka), containerized deployment (Kubernetes).

---

## Diagrams
- **Domain Model**: See `diagrams/domain.puml` for class relationships and entity structure.
- **Module Architecture**: See `diagrams/arquitectura-outbox.puml` for module interactions and data flow.

---

## References
- [Domain Model Diagram](diagrams/domain.puml)
- [Module Architecture Diagram](diagrams/arquitectura-outbox.puml)

---

For further details, consult the referenced diagrams and specifications.

