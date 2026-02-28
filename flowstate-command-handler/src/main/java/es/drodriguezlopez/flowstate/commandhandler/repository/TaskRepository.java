package es.drodriguezlopez.flowstate.commandhandler.repository;

import es.drodriguezlopez.flowstate.commandhandler.domain.Priority;
import es.drodriguezlopez.flowstate.commandhandler.domain.Status;
import es.drodriguezlopez.flowstate.commandhandler.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Repository interface for managing {@link Task} entities.
 * <p>
 * Provides custom update operations for task priority and status using JPQL queries with named parameters.
 */
public interface TaskRepository extends JpaRepository<Task, UUID> {
    /**
     * Updates the priority of a task identified by its ID.
     *
     * @param id      the unique identifier of the task
     * @param priority the new priority to set
     * @return the number of affected rows
     */
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.priority = :priority WHERE t.id = :id")
    int updatePriorityById(@Param("id") UUID id, @Param("priority") Priority priority);

    /**
     * Updates the status of a task identified by its ID.
     *
     * @param id     the unique identifier of the task
     * @param status the new status to set
     * @return the number of affected rows
     */
    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :id")
    int updateStatusById(@Param("id") UUID id, @Param("status") Status status);
}
