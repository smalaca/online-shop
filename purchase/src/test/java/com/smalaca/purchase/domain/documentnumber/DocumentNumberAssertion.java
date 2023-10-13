package com.smalaca.purchase.domain.documentnumber;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentNumberAssertion {
    private static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    private final Object actual;
    private final String fieldName;

    private DocumentNumberAssertion(Object actual, String fieldName) {
        this.actual = actual;
        this.fieldName = fieldName;
    }

    public static DocumentNumberAssertion assertDocumentNumber(Object actual) {
        return assertDocumentNumber(actual, "documentNumber");
    }

    public static DocumentNumberAssertion assertDocumentNumber(Object actual, String fieldName) {
        return new DocumentNumberAssertion(actual, fieldName);
    }

    public void hasDocumentNumberThatStartsWith(String expected) {
        assertThat(actual).extracting(fieldName).extracting("value")
                .satisfies(offerNumber -> {
                    String actualOfferNumber = (String) offerNumber;
                    assertThat(actualOfferNumber).matches(Pattern.compile("^" + expected + UUID_REGEX + "$"));
                });
    }
}
