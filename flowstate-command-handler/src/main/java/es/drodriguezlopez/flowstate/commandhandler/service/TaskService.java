package es.drodriguezlopez.flowstate.commandhandler.service;

import es.drodriguezlopez.flowstate.commandhandler.model.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskResponse;
import es.drodriguezlopez.flowstate.commandhandler.model.UpdateTaskStatusRequest;

import java.util.UUID;

/**
 * Service interface for managing tasks in the Flowstate Command API.
 * Provides methods to create tasks, adjust their priority, and update their status.
 */
public interface TaskService {

    /**
     * Creates a new task based on the provided request.
     *
     * @param taskRequest the request containing task details
     * @return the created task response
     */
    TaskResponse createTask(TaskRequest taskRequest);

    /**
     * Adjusts the priority of an existing task.
     *
     * @param taskId the unique identifier of the task
     * @param request the request containing the new priority information
     */
    void adjustTaskPriority(UUID taskId, AdjustTaskPriorityRequest request);

    /**
     * Updates the status of an existing task.
     *
     * @param taskId the unique identifier of the task
     * @param request the request containing the new status information
     */
    void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest request);
}
