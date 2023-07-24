package com.smalaca.appthree;

import com.smalaca.appone.DataTransferObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("query")
public class Controller {
    private final List<DataTransferObject> dataTransferObjects = new ArrayList<>();

    @GetMapping
    public List<DataTransferObject> world() {
        return dataTransferObjects;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}", containerFactory = "listenerContainerFactory")
    public void listenGroupOneOdd(DataTransferObject dto) {
        dataTransferObjects.add(dto);
        System.out.println("Received: " + dto);
    }
}
