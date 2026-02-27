package es.drodriguezlopez.flowstate.commandhandler.service;

import es.drodriguezlopez.flowstate.commandhandler.contract.AdjustTaskPriorityRequest;
import es.drodriguezlopez.flowstate.commandhandler.contract.TaskRequest;
import es.drodriguezlopez.flowstate.commandhandler.contract.TaskResponse;
import es.drodriguezlopez.flowstate.commandhandler.contract.UpdateTaskStatusRequest;
import es.drodriguezlopez.flowstate.commandhandler.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        return null;
    }

    @Override
    public void adjustTaskPriority(UUID taskId, AdjustTaskPriorityRequest request) {

    }

    @Override
    public void updateTaskStatus(UUID taskId, UpdateTaskStatusRequest request) {

    }
}
