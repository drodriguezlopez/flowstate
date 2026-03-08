package es.drodriguezlopez.flowstate.commandhandler.service;

import es.drodriguezlopez.flowstate.commandhandler.domain.Task;
import es.drodriguezlopez.flowstate.commandhandler.mapper.TaskMapper;
import es.drodriguezlopez.flowstate.commandhandler.model.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskResponse;
import es.drodriguezlopez.flowstate.commandhandler.model.UpdateTaskStatusRequest;
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
    private final TaskMapper taskMapper;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = taskRepository.save(taskMapper.toDomain(taskRequest));
        TaskResponse response = taskMapper.toTaskResponse(task);
        log.debug("createTask completed for taskRequest: {} with response: {}", taskRequest, response);
        return response;
    }

    @Override
    public void adjustTaskPriority(UUID taskId, AdjustTaskPriorityRequest request) {
        taskRepository.updatePriorityById(taskId, taskMapper.toDomain(request.getPriority()));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        log.debug("adjustTaskPriority completed for taskId: {} with request: {}", taskId, request);
    }

    @Override
    public void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest request) {
        taskRepository.updateStatusById(taskId, taskMapper.toDomain(request.getStatus()));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));
        log.debug("updateTaskStatus completed for taskId: {} with request: {}", taskId, request);
    }
}
