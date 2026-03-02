package es.drodriguezlopez.flowstate.commandhandler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Outbox journal entry for Task domain events.
 * <p>
 * Every write operation on a {@link Task} (create, status update, priority adjustment)
 * appends a corresponding {@code TaskOutboxEvent} row within the <em>same</em> database
 * transaction, guaranteeing at-least-once delivery to the message broker via the
 * {@code flowstate-event-relay} CDC module.
 *
 * <p>Column reference:
 * <ul>
 *   <li>{@code id}           – Unique event identifier (UUID v4).</li>
 *   <li>{@code aggregateId}  – ID of the {@link Task} that produced the event.</li>
 *   <li>{@code eventType}    – Discriminator string, e.g. {@code TASK_CREATED}.</li>
 *   <li>{@code payload}      – JSON-serialised event body consumed by the relay.</li>
 *   <li>{@code createdAt}    – Wall-clock timestamp of insertion.</li>
 *   <li>{@code dispatched}   – Set to {@code true} once the relay has forwarded the event.</li>
 *   <li>{@code dispatchedAt} – Timestamp of successful dispatch (nullable).</li>
 * </ul>
 */
@Entity
@Table(name = "TASK_OUTBOX_EVENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskOutboxEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "AGGREGATE_ID", nullable = false)
    private UUID aggregateId;

    @Column(name = "EVENT_TYPE", nullable = false, length = 128)
    private String eventType;

    /** JSON payload serialised from the task snapshot at the time of the operation. */
    @Lob
    @Column(name = "PAYLOAD", nullable = false)
    private String payload;

    @Column(name = "CREATED_AT", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "DISPATCHED", nullable = false)
    @Builder.Default
    private boolean dispatched = false;

    @Column(name = "DISPATCHED_AT")
    private LocalDateTime dispatchedAt;
}

