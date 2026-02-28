package es.drodriguezlopez.flowstate.queryhandler.mapper;

import es.drodriguezlopez.flowstate.queryhandler.domain.Task;
import es.drodriguezlopez.flowstate.queryhandler.model.TaskSummaryResponse;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    /**
     * Maps a Task entity to a TaskSummaryResponse DTO.
     *
     * @param task the task entity to map
     * @return the mapped TaskSummaryResponse
     */
    public abstract TaskSummaryResponse toTaskSummaryResponse(Task task);
}
