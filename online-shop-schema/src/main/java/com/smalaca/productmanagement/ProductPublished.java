package com.smalaca.productmanagement;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.util.UUID;

public record ProductPublished(UUID eventId, LocalDate creationDate, String name, int price) {
    private static final Faker FAKER = new Faker();

    public static ProductPublished random() {
        return new ProductPublished(
                UUID.randomUUID(),
                FAKER.date().birthday().toLocalDateTime().toLocalDate(),
                FAKER.book().title(),
                FAKER.number().positive());
    }
}
