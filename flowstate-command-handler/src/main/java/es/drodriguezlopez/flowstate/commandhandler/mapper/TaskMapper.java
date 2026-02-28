package es.drodriguezlopez.flowstate.commandhandler.mapper;


import es.drodriguezlopez.flowstate.commandhandler.domain.Task;
import es.drodriguezlopez.flowstate.commandhandler.model.Priority;
import es.drodriguezlopez.flowstate.commandhandler.model.Status;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskRequest;
import es.drodriguezlopez.flowstate.commandhandler.model.TaskResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    public abstract Task toDomain(TaskRequest request);

    public abstract TaskResponse toTaskResponse(Task task);

    public abstract es.drodriguezlopez.flowstate.commandhandler.domain.Priority toDomain(Priority priority);

    public abstract es.drodriguezlopez.flowstate.commandhandler.domain.Status toDomain(Status status);
}
