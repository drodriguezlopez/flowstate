package es.drodriguezlopez.flowstate.queryhandler.service;

import es.drodriguezlopez.flowstate.queryhandler.mapper.TaskMapper;
import es.drodriguezlopez.flowstate.queryhandler.model.TaskSummaryResponse;
import es.drodriguezlopez.flowstate.queryhandler.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskSummaryResponse> getTaskById(UUID taskId) {
        log.debug("Fetching task with ID: {}", taskId);
        return taskRepository.findById(taskId)
                .map(taskMapper::toTaskSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskSummaryResponse> getTaskSummaries() {
        log.debug("Fetching all task summaries");
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskSummaryResponse)
                .toList();
    }
}
