package com.smalaca.purchase.domain.documentnumber;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static java.time.format.DateTimeFormatter.ofPattern;

@ValueObject
@EqualsAndHashCode
public final class DocumentNumber {
    private static final String SEPARATOR = "/";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ofPattern("yyyy" + SEPARATOR + "MM" + SEPARATOR + "dd");
    private static final String ORDER = "Order";
    private static final String OFFER = "Offer";
    private static final String PURCHASE = "Purchase";

    private final String value;

    private DocumentNumber(String value) {
        this.value = value;
    }

    public static DocumentNumber offerNumber(UUID buyerId, LocalDateTime creationDateTime) {
        return create(OFFER, buyerId, creationDateTime);
    }

    public static DocumentNumber orderNumber(UUID buyerId, LocalDateTime creationDateTime) {
        return create(ORDER, buyerId, creationDateTime);
    }

    public static DocumentNumber purchaseNumber(UUID buyerId, LocalDateTime creationDateTime) {
        return create(PURCHASE, buyerId, creationDateTime);
    }

    private static DocumentNumber create(String documentType, UUID buyerId, LocalDateTime creationDateTime) {
        String date = creationDateTime.format(DATE_TIME_FORMATTER);
        return new DocumentNumber(documentType + SEPARATOR + buyerId + SEPARATOR + date + SEPARATOR + UUID.randomUUID());
    }
}
