# FlowState – Functional Domain Model

> ← Back to [Documentation Index](../README.md)

This document describes the core domain entities and concepts that drive the FlowState task management system, covering the aggregate root, read models, and audit log as used in the CQRS architecture.

---

## 1. Task (Aggregate Root / Source of Truth)

The **Task** is the central domain entity and the primary unit of consistency in the system. It holds the current state and enforces all business rules. On the write (command) side, the application loads this aggregate to perform operations such as creating, updating, or completing a task.

* **Attributes:** Unique ID (`TaskId`), Title, detailed Description, current Status (e.g., `TO_DO`, `IN_PROGRESS`, `DONE`), and Priority level.
* **Business Logic:** Methods such as `markAsCompleted()` or `changePriority()` validate whether the requested transition is permitted before mutating state.
* **Key Operations:**
    * **Updating Status:** Ensures a task cannot be moved to `DONE` unless it has previously been `IN_PROGRESS`.
    * **Adjusting Priority:** Validates urgency levels according to the domain rules.

---

## 2. Task Summary (Read Model / Display View / DTO)

The **Task Summary** is a lightweight, read-only projection of a task, optimised for speed and display. In the CQRS architecture, data is projected into a separate, denormalised representation suited for specific screens or consumers.

* **Attributes:** Unique ID (`TaskId`), Title, Status, and Due Date.
* **Purpose:** Acts as a read-only snapshot for lists, dashboards, and mobile views. It carries no business logic; it exists solely to serve data efficiently.

---

## 3. Task History (Event / Audit Log / Timeline)

The **Task History** records every state change that has occurred over the lifetime of a task, providing a full and immutable audit trail.

* **Attributes:** History ID (`HistoryId`), the associated Task (`TaskId`), the Action taken (e.g., `CREATED`, `STATUS_CHANGED`), a Timestamp, and the User who performed the action (`PerformedBy`).
* **Purpose:** Provides a complete audit trail, enabling managers and operators to reconstruct the timeline of any task — capturing who did what and when.

---

## Separation of Concerns in Practice

| Concern | Component | Role |
|---|---|---|
| Write / State mutation | `Task` aggregate | Enforces invariants; appended to outbox on every change |
| Read / Display | `TaskSummary` | Denormalised projection used by dashboards and list views |
| Audit / History | `TaskHistory` | Immutable event log for traceability and compliance |
