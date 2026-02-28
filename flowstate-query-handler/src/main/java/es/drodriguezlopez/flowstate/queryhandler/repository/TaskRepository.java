package es.drodriguezlopez.flowstate.queryhandler.repository;

import es.drodriguezlopez.flowstate.queryhandler.domain.Priority;
import es.drodriguezlopez.flowstate.queryhandler.domain.Status;
import es.drodriguezlopez.flowstate.queryhandler.domain.Task;
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

}
