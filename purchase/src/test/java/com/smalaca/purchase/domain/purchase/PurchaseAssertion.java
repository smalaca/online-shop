package com.smalaca.purchase.domain.purchase;

import com.smalaca.purchase.domain.price.Price;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.smalaca.purchase.domain.documentnumber.DocumentNumberAssertion.assertDocumentNumber;
import static org.assertj.core.api.Assertions.assertThat;

public class PurchaseAssertion {
    private final Purchase actual;

    private PurchaseAssertion(Purchase actual) {
        this.actual = actual;
    }

    public static PurchaseAssertion assertPurchase(Purchase actual) {
        return new PurchaseAssertion(actual);
    }

    public PurchaseAssertion hasOrderId(UUID expected) {
        assertThat(actual).extracting("orderId").isEqualTo(expected);
        return this;
    }

    public PurchaseAssertion hasPurchaseNumberThatStartsWith(String expected) {
        assertDocumentNumber(actual).hasDocumentNumberThatStartsWith(expected);
        return this;
    }

    public PurchaseAssertion hasCreationDateTime(LocalDateTime expected) {
        assertThat(actual).extracting("creationDateTime").isEqualTo(expected);
        return this;
    }

    public PurchaseAssertion hasBuyerId(UUID expected) {
        assertThat(actual).extracting("buyerId").isEqualTo(expected);
        return this;
    }

    public PurchaseAssertion hasPaymentMethod(UUID expected) {
        assertThat(actual).extracting("paymentMethodId").isEqualTo(expected);
        return this;
    }

    public PurchaseAssertion hasTotalPrice(Price expected) {
        assertThat(actual).extracting("totalPrice").isEqualTo(expected);
        return this;
    }
}
