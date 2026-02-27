package es.drodriguezlopez.flowstate.commandhandler.repository;

import es.drodriguezlopez.flowstate.commandhandler.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}

