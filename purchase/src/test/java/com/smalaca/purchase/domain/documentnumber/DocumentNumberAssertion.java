package com.smalaca.purchase.domain.documentnumber;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class DocumentNumberAssertion {
    private static final String UUID_REGEX = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    private final Object actual;

    private DocumentNumberAssertion(Object actual) {
        this.actual = actual;
    }

    public static DocumentNumberAssertion assertDocumentNumber(Object actual) {
        return new DocumentNumberAssertion(actual);
    }

    public void hasDocumentNumberThatStartsWith(String expected) {
        assertThat(actual).extracting("documentNumber").extracting("value")
                .satisfies(offerNumber -> {
                    String actualOfferNumber = (String) offerNumber;
                    assertThat(actualOfferNumber).matches(Pattern.compile("^" + expected + UUID_REGEX + "$"));
                });
    }
}
