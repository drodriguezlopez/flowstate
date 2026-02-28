package es.drodriguezlopez.flowstate.queryhandler.controller;

import es.drodriguezlopez.flowstate.queryhandler.api.TasksApi;
import es.drodriguezlopez.flowstate.queryhandler.model.TaskSummaryResponse;
import es.drodriguezlopez.flowstate.queryhandler.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TasksController implements TasksApi {

    private final TaskService taskService;

    @Override
    public TaskSummaryResponse getTaskById(UUID taskId) {
        log.info("Entering getTaskById with taskId: {}", taskId);
        TaskSummaryResponse response = taskService.getTaskById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found with taskId: {}", taskId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
                });
        log.debug("getTaskById completed for taskId: {}", taskId);
        return response;
    }

    @Override
    public List<TaskSummaryResponse> getTaskSummaries() {
        log.info("Entering getTaskSummaries");
        List<TaskSummaryResponse> response = taskService.getTaskSummaries();
        log.debug("getTaskSummaries completed with {} tasks", response.size());
        return response;
    }
}
