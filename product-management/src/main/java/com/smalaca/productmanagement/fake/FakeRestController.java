package com.smalaca.productmanagement.fake;

import com.smalaca.productmanagement.ProductPublished;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("product")
public class FakeRestController {
    private final List<ProductPublished> products = new ArrayList<>();
    private final KafkaTemplate<String, ProductPublished> kafkaTemplate;
    private final String topicName;

    FakeRestController(@Value("${kafka.topic}") String topicName, KafkaTemplate<String, ProductPublished> kafkaTemplate) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/all")
    public List<ProductPublished> all() {
        return products;
    }

    @GetMapping("/add")
    public ProductPublished addRandom() {
        ProductPublished random = ProductPublished.random();
        products.add(random);
        return random;
    }

    @GetMapping("/publish")
    public String publish() {
        ProductPublished random = addRandom();
        kafkaTemplate.send(topicName, random);
        return "Send to: " + topicName + " message: " + random;
    }
}
