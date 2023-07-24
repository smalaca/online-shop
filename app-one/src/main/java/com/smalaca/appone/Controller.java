package com.smalaca.appone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class Controller {
    private final KafkaTemplate<String, DataTransferObject> kafkaTemplate;
    private final String topicName;

    Controller(@Value("${kafka.topic}") String topicName, KafkaTemplate<String, DataTransferObject> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping
    public String publish() {
        DataTransferObject dataTransferObject = DataTransferObject.random();
        kafkaTemplate.send(topicName, dataTransferObject);
        return "Send to: " + topicName + " message: " + dataTransferObject;
    }
}
