package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ValueObject
@EqualsAndHashCode
class OrderNumber {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final String value;

    private OrderNumber(String value) {
        this.value = value;
    }

    static OrderNumber orderNumber(UUID buyerId, LocalDateTime creationDateTime) {
        String date = creationDateTime.format(DATE_TIME_FORMATTER);
        return new OrderNumber("Order/" + buyerId + "/" + date + "/" + UUID.randomUUID());
    }
}
