### 1. `Task` (The Aggregate Root)

This is the heart of your domain. It holds the current state and enforces business rules. In CQRS, the "Write" side of your app will load this class to perform actions like creating, updating, or completing a task.

* **Attributes:** `TaskId`, `Title`, `Description`, `Status` (e.g., Todo, InProgress, Done), `Priority`.
* **Business Logic:** Methods like `MarkAsCompleted()` or `ChangePriority()` that check if the transition is allowed before updating the state.

### 2. `TaskSummary` (The Read Model/DTO)

Unlike the `Task` class, which is built for logic, this class is built purely for display. In a CQRS architecture, you often project data into a separate, flat table or object optimized for specific screens.

* **Attributes:** `TaskId`, `Title`, `Status`, `DueDate`.
* **Purpose:** This is what your "Query" side will return. It doesn't contain logic; it just holds data to be displayed quickly in a list view.

### 3. `TaskHistory` (The Event/Audit Log)

CQRS pairs beautifully with Event Sourcing. Having a class that tracks what happened to a task is great for a demo to show the "Audit" side of a system.

* **Attributes:** `HistoryId`, `TaskId`, `ActionType` (e.g., "Created", "StatusChanged"), `Timestamp`, `PerformedBy`.
* **Purpose:** This captures the "what" and "when," allowing you to show a timeline view without bloating your main `Task` aggregate.

---

### Why this works for a demo

* **Separation:** You can show how a `CreateTaskCommand` updates the `Task` aggregate, while a `GetTaskDashboardQuery` reads from a completely different `TaskSummary` table.
* **Complexity:** It is simple enough to code quickly but complex enough to show why you wouldn't want to use the same class for saving and reading.

Would you like me to generate a simple code scaffold (in a language of your choice) to show how a command handler would interact with the `Task` aggregate?