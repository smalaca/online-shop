package com.smalaca.productcatalogue.fake;

import com.smalaca.productmanagement.ProductPublished;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("product")
public class FakeRestController {
    private final List<ProductPublished> products = new ArrayList<>();

    @KafkaListener(topics = "${kafka.topic}", groupId = "${kafka.group-id}", containerFactory = "listenerContainerFactory")
    public void listenGroupOneOdd(ProductPublished dto) {
        products.add(dto);
        System.out.println("Received: " + dto);
    }

    @GetMapping("/all")
    public List<ProductPublished> all() {
        return products;
    }
}
