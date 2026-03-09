package es.drodriguezlopez.flowstate.queryhandler.listener.model;

import es.drodriguezlopez.flowstate.queryhandler.domain.Priority;
import es.drodriguezlopez.flowstate.queryhandler.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Data
public class Task {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
}
