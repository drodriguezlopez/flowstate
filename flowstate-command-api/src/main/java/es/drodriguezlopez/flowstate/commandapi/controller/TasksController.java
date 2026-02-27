package es.drodriguezlopez.flowstate.commandapi.controller;

import es.drodriguezlopez.flowstate.commandapi.api.TasksApi;
import es.drodriguezlopez.flowstate.commandapi.client.CommandHandlerClient;
import es.drodriguezlopez.flowstate.commandapi.contract.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandapi.contract.TaskRequest;
import es.drodriguezlopez.flowstate.commandapi.contract.TaskResponse;
import es.drodriguezlopez.flowstate.commandapi.contract.UpdateTaskStatusRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TasksController implements TasksApi {

    private final CommandHandlerClient commandHandlerClient;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        log.info("Entering createTask with taskRequest: {}", taskRequest);
        TaskResponse response = commandHandlerClient.createTask(taskRequest);
        log.debug("createTask completed for taskRequest: {}", taskRequest);
        return response;
    }

    @Override
    public void adjustTaskPriority(UUID taskId, AdjustTaskPriorityRequest adjustTaskPriorityRequest) {
        log.info("Entering adjustTaskPriority with taskId: {}, adjustTaskPriorityRequest: {}", taskId, adjustTaskPriorityRequest);
        commandHandlerClient.adjustTaskPriority(taskId, adjustTaskPriorityRequest);
        log.debug("adjustTaskPriority completed for taskId: {}", taskId);
    }

    @Override
    public void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest updateTaskStatusRequest) {
        log.info("Entering updateTaskStatus with taskId: {}, updateTaskStatusRequest: {}", taskId, updateTaskStatusRequest);
        commandHandlerClient.updateTaskStatus(taskId, updateTaskStatusRequest);
        log.debug("updateTaskStatus completed for taskId: {}", taskId);
    }
}
