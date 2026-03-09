package es.drodriguezlopez.flowstate.queryhandler.service;


import es.drodriguezlopez.flowstate.queryhandler.listener.model.Task;
import es.drodriguezlopez.flowstate.queryhandler.mapper.TaskMapper;
import es.drodriguezlopez.flowstate.queryhandler.model.TaskSummaryResponse;
import es.drodriguezlopez.flowstate.queryhandler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Optional<TaskSummaryResponse> getTaskById(UUID taskId) {
        log.debug("Fetching task with ID: {}", taskId.toString());
        return taskRepository.findById(taskId.toString())
                .map(taskMapper::toTaskSummaryResponse);
    }

    @Override
    public List<TaskSummaryResponse> getTaskSummaries() {
        log.debug("Fetching all task summaries");
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskSummaryResponse)
                .toList();
    }

    @Override
    public void save(Task task) {
        taskRepository.save(taskMapper.toTaskDomain(task));
    }
}


