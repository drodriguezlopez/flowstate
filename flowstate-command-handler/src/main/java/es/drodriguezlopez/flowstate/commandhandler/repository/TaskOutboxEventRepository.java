package es.drodriguezlopez.flowstate.commandhandler.repository;

import es.drodriguezlopez.flowstate.commandhandler.domain.TaskOutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for managing {@link TaskOutboxEvent} entries.
 * <p>
 * Used by:
 * <ul>
 *   <li>{@code TaskServiceImpl} – to append events atomically with task mutations.</li>
 *   <li>{@code flowstate-event-relay} – to poll pending events and mark them as dispatched.</li>
 * </ul>
 */
public interface TaskOutboxEventRepository extends JpaRepository<TaskOutboxEvent, UUID> {

    /**
     * Returns all events that have not yet been forwarded to the message broker,
     * ordered by insertion time (oldest first).
     *
     * @return list of pending outbox events
     */
    List<TaskOutboxEvent> findByDispatchedFalseOrderByCreatedAtAsc();

    /**
     * Marks a single outbox event as dispatched.
     *
     * @param id           the event identifier
     * @param dispatchedAt the timestamp of successful dispatch
     * @return number of rows updated
     */
    @Modifying
    @Transactional
    @Query("UPDATE TaskOutboxEvent e SET e.dispatched = true, e.dispatchedAt = :dispatchedAt WHERE e.id = :id")
    int markAsDispatched(@Param("id") UUID id, @Param("dispatchedAt") LocalDateTime dispatchedAt);
}

