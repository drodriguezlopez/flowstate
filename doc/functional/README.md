# Task Management System

This document merges the core concepts and terminology from both the functional specification and the README, providing a comprehensive overview of the Task Management System and its CQRS architecture.

---

## 1. Task (Aggregate Root / Source of Truth)

The **Task** is the heart of your domain and the primary engine of the system. It holds the current state and enforces business rules. In CQRS, the "Write" side of your app will load this class to perform actions like creating, updating, or completing a task.

* **Attributes:** Unique ID (`TaskId`), Title, detailed Description, current Status (e.g., To Do, In Progress, Done), and Priority level.
* **Business Logic:** Methods like `MarkAsCompleted()` or `ChangePriority()` that check if the transition is allowed before updating the state.
* **Key Operations:**
    * **Updating Status:** Ensures a task can’t be "Completed" if it hasn't been "Started."
    * **Adjusting Priority:** Validates urgency levels based on organizational rules.

---

## 2. Task Summary (Read Model / Display View / DTO)

The **Task Summary** is a lightweight, read-only version of a task, built specifically for speed and visibility. In a CQRS architecture, you often project data into a separate, flat table or object optimized for specific screens.

* **Attributes:** Unique ID (`TaskId`), Title, Status, and Due Date.
* **Purpose:** It acts as a "read-only" snapshot for lists, dashboards, and mobile views. It doesn't contain logic; it just holds data to be displayed quickly.

---

## 3. Task History (Event / Audit Log / Timeline)

The **Task History** serves as the system's memory, tracking every change that has occurred over time. CQRS pairs beautifully with Event Sourcing, and having a class that tracks what happened to a task is great for audit purposes.

* **Attributes:** History ID (`HistoryId`), the Task affected (`TaskId`), the Action taken (e.g., "Created", "StatusChanged"), a Timestamp, and the User who performed the action (`PerformedBy`).
* **Purpose:** This provides a full audit trail, allowing managers to see a timeline of progress and understand the "who, what, and when" behind every update.

---

## Why this works for a demo

* **Separation:** You can show how a `CreateTaskCommand` updates the `Task` aggregate, while a `GetTaskDashboardQuery` reads from a completely different `TaskSummary` table.
* **Complexity:** It is simple enough to code quickly but complex enough to show why you wouldn't want to use the same class for saving and reading.

---

Would you like me to generate a simple code scaffold (in Java or another language) to show how a command handler would interact with the `Task` aggregate?

