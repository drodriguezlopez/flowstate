package es.drodriguezlopez.flowstate.queryhandler.repository;

import es.drodriguezlopez.flowstate.queryhandler.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

/**
 * Repository interface for managing {@link Task} documents in MongoDB.
 */
public interface TaskRepository extends MongoRepository<Task, String> {

}
