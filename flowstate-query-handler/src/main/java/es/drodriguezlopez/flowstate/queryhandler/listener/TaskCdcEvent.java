package es.drodriguezlopez.flowstate.queryhandler.listener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.drodriguezlopez.flowstate.queryhandler.domain.Task;
import lombok.Data;

import java.util.UUID;

/**
 * Represents a Debezium CDC event payload for the Task entity.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskCdcEvent {

    private Payload payload;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Payload {
        private Task before;
        private Task after;
        private String op;
    }
}

