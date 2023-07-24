package com.smalaca.appone;

import java.time.LocalDate;
import java.util.UUID;

public record DataTransferObject(UUID eventId, LocalDate creationDate, String author, String message) {}
