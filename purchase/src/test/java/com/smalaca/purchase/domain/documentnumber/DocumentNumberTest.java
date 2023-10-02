package com.smalaca.purchase.domain.documentnumber;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class DocumentNumberTest {

    @Test
    void shouldFulFillEqualsAndHashCodeContract() {
        EqualsVerifier.forClass(DocumentNumber.class).verify();
    }
}