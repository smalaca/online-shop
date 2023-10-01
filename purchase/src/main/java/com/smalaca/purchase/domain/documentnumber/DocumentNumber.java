package com.smalaca.purchase.domain.documentnumber;

import com.smalaca.annotations.ddd.ValueObject;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@ValueObject
@EqualsAndHashCode
public class DocumentNumber {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final String value;

    private DocumentNumber(String value) {
        this.value = value;
    }

    public static DocumentNumber offerNumber(UUID buyerId, LocalDateTime creationDateTime) {
        String date = creationDateTime.format(DATE_TIME_FORMATTER);
        return new DocumentNumber("Offer/" + buyerId + "/" + date + "/" + UUID.randomUUID());
    }
}
