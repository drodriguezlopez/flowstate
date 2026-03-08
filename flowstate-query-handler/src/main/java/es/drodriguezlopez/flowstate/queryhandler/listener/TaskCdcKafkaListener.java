package es.drodriguezlopez.flowstate.queryhandler.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.drodriguezlopez.flowstate.queryhandler.domain.Task;
import es.drodriguezlopez.flowstate.queryhandler.repository.TaskRepository;
import es.drodriguezlopez.flowstate.queryhandler.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka listener that consumes Debezium CDC events from the Task topic
 * and materialises the read-model in MongoDB.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TaskCdcKafkaListener {

    private final ObjectMapper objectMapper;
    private final TaskService taskService;

    @KafkaListener(
            topics = "${flowstate.kafka.cdc.topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onTaskCdcEvent(String event) {
        log.debug("Received CDC message: {}", event);
        try {
            TaskCdcEvent message = objectMapper.readValue(event, TaskCdcEvent.class);


            switch (message.getPayload().getOp()) {
                case "c", "r" -> upsertTask(message.getPayload().getAfter());
                case "u" -> upsertTask(message.getPayload().getAfter());
                case "d" -> deleteTask(message.getPayload().getBefore());
                default -> log.warn("Unknown CDC operation '{}', skipping message", message.getPayload().getOp());
            }
        } catch (Exception e) {
            log.error("Error processing CDC message: {}", event, e);
        }
    }

    // -------------------------------------------------------------------------

    private void upsertTask(Task task) {
        taskService.save(task);
        log.info("Task upserted in read-store: id={}", task.getId());
    }

    private void deleteTask(Task task) {

        log.info("Task deleted from read-store: id={}", task.getId());
    }


}

