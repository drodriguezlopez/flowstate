package es.drodriguezlopez.flowstate.commandapi.client;

import es.drodriguezlopez.flowstate.commandapi.contract.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandapi.contract.TaskRequest;
import es.drodriguezlopez.flowstate.commandapi.contract.TaskResponse;
import es.drodriguezlopez.flowstate.commandapi.contract.UpdateTaskStatusRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface CommandHandlerClient {
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
