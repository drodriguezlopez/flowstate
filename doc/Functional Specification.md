## Functional Specification: Task Management System 

This document outlines the core components of the Task Management System.

---

### 1. The Core Task (The "Source of Truth")

The **Task** is the primary engine of the system. It is responsible for holding the current state of work and ensuring that all changes follow specific business rules. When you want to "do" something—like start a project or change a deadline—this component handles the logic.

* **Information Tracked:** Unique ID, Title, detailed Description, current Status (e.g., To Do, In Progress, Done), and Priority level.
* **Key Operations:**
* **Updating Status:** Ensures a task can’t be "Completed" if it hasn't been "Started."
* **Adjusting Priority:** Validates urgency levels based on organizational rules.

---

### 2. Task Summary (The "Display View")

The **Task Summary** is a lightweight version of a task, built specifically for speed and visibility. While the "Core Task" handles the heavy thinking, the Summary is designed to be pulled into lists, dashboards, and mobile views instantly.

* **Information Tracked:** Unique ID, Title, Status, and Due Date.
* **Purpose:** It acts as a "read-only" snapshot. It doesn't perform calculations or check rules; it simply delivers data to the user's screen as quickly as possible.

---

### 3. Task History (The "Timeline")

The **Task History** serves as the system's memory. Instead of just knowing what the task looks like *now*, this component tracks every change that has occurred over time.

* **Information Tracked:** History ID, the Task affected, the Action taken (e.g., "Status Changed"), a Timestamp, and the User who performed the action.
* **Purpose:** This provides a full audit trail. It allows managers to see a timeline of progress and understand the "who, what, and when" behind every update.

