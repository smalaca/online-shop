package com.smalaca.appone;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.UUID;

public record DataTransferObject(UUID eventId, LocalDate creationDate, String author, String message) {
    private static final Faker FAKER = new Faker();

    static DataTransferObject random() {
        return new DataTransferObject(
                UUID.randomUUID(),
                FAKER.date().birthday().toLocalDateTime().toLocalDate(),
                FAKER.superhero().name(),
                FAKER.lorem().sentence());
    }
}
