package es.drodriguezlopez.flowstate.commandhandler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.drodriguezlopez.flowstate.commandhandler.domain.Task;
import es.drodriguezlopez.flowstate.commandhandler.domain.TaskOutboxEvent;
import es.drodriguezlopez.flowstate.commandhandler.mapper.TaskMapper;
import es.drodriguezlopez.flowstate.commandhandler.model.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskResponse;
import es.drodriguezlopez.flowstate.commandhandler.model.UpdateTaskStatusRequest;
import es.drodriguezlopez.flowstate.commandhandler.repository.TaskOutboxEventRepository;
import es.drodriguezlopez.flowstate.commandhandler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskOutboxEventRepository taskOutboxEventRepository;
    private final TaskMapper taskMapper;
    private final ObjectMapper objectMapper;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = taskRepository.save(taskMapper.toDomain(taskRequest));
        appendOutboxEvent(task.getId(), "TASK_CREATED", task);
        TaskResponse response = taskMapper.toTaskResponse(task);
        log.debug("createTask completed for taskRequest: {} with response: {}", taskRequest, response);
        return response;
    }

    @Override
    public void adjustTaskPriority(UUID taskId, AdjustTaskPriorityRequest request) {
        taskRepository.updatePriorityById(taskId, taskMapper.toDomain(request.getPriority()));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        appendOutboxEvent(taskId, "TASK_PRIORITY_ADJUSTED", task);
        log.debug("adjustTaskPriority completed for taskId: {} with request: {}", taskId, request);
    }

    @Override
    public void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest request) {
        taskRepository.updateStatusById(taskId, taskMapper.toDomain(request.getStatus()));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        appendOutboxEvent(taskId, "TASK_STATUS_UPDATED", task);
        log.debug("updateTaskStatus completed for taskId: {} with request: {}", taskId, request);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    /**
     * Appends a {@link TaskOutboxEvent} row within the current transaction so that
     * the task mutation and its event record are committed atomically.
     *
     * @param aggregateId the task ID that produced the event
     * @param eventType   discriminator string (e.g. {@code TASK_CREATED})
     * @param payload     domain object to serialise as JSON
     */
    private void appendOutboxEvent(UUID aggregateId, String eventType, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            taskOutboxEventRepository.save(
                    TaskOutboxEvent.builder()
                            .aggregateId(aggregateId)
                            .eventType(eventType)
                            .payload(json)
                            .build()
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialise outbox event payload for " + eventType, e);
        }
    }
}
