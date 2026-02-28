package es.drodriguezlopez.flowstate.commandhandler.controller;

import es.drodriguezlopez.flowstate.commandhandler.api.TasksApi;
import es.drodriguezlopez.flowstate.commandhandler.model.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskResponse;
import es.drodriguezlopez.flowstate.commandhandler.model.UpdateTaskStatusRequest;
import es.drodriguezlopez.flowstate.commandhandler.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TasksController implements TasksApi {

    private final TaskService taskService;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        log.info("Entering createTask with taskRequest: {}", taskRequest);
        TaskResponse response = taskService.createTask(taskRequest);
        log.debug("createTask completed for taskRequest: {}", taskRequest);
        return response;
    }

    @Override
    public void adjustTaskPriority(UUID taskId, AdjustTaskPriorityRequest adjustTaskPriorityRequest) {
        log.info("Entering adjustTaskPriority with taskId: {}, adjustTaskPriorityRequest: {}", taskId, adjustTaskPriorityRequest);
        taskService.adjustTaskPriority(taskId, adjustTaskPriorityRequest);
        log.debug("adjustTaskPriority completed for taskId: {}", taskId);
    }

    @Override
    public void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest updateTaskStatusRequest) {
        log.info("Entering updateTaskStatus with taskId: {}, updateTaskStatusRequest: {}", taskId, updateTaskStatusRequest);
        taskService.updateTaskStatus(taskId, updateTaskStatusRequest);
        log.debug("updateTaskStatus completed for taskId: {}", taskId);
    }
}
