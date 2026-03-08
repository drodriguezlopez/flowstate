package es.drodriguezlopez.flowstate.queryhandler.service;

import es.drodriguezlopez.flowstate.queryhandler.domain.Task;
import es.drodriguezlopez.flowstate.queryhandler.model.TaskSummaryResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {

    /**
     * Retrieves a single task summary by its ID.
     *
     * @param taskId the unique identifier of the task
     * @return an Optional containing the TaskSummaryResponse if found
     */
    Optional<TaskSummaryResponse> getTaskById(UUID taskId);

    /**
     * Retrieves all task summaries.
     *
     * @return a list of all TaskSummaryResponse objects
     */
    List<TaskSummaryResponse> getTaskSummaries();

    void save(Task task);
}
