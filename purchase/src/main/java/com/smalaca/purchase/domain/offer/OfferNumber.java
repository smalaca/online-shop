package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ValueObject
@EqualsAndHashCode
class OfferNumber {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final String value;

    private OfferNumber(String value) {
        this.value = value;
    }

    static OfferNumber offerNumber(UUID buyerId, LocalDateTime creationDateTime) {
        String date = creationDateTime.format(DATE_TIME_FORMATTER);
        return new OfferNumber("Offer/" + buyerId + "/" + date + "/" + UUID.randomUUID());
    }
}
