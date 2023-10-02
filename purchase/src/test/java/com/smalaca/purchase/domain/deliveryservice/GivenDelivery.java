package com.smalaca.purchase.domain.deliveryservice;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.price.Price;

import java.util.UUID;

import static com.smalaca.purchase.domain.deliveryservice.DeliveryStatusCode.NOT_EXISTING_ADDRESS;
import static com.smalaca.purchase.domain.deliveryservice.DeliveryStatusCode.SUCCESS;
import static com.smalaca.purchase.domain.deliveryservice.DeliveryStatusCode.UNSUPPORTED_METHOD;
import static org.mockito.BDDMockito.given;

public class GivenDelivery {
    private static final Price NO_PRICE = null;

    private final DeliveryService deliveryService;
    private final UUID deliveryMethodId;
    private final DeliveryAddress deliveryAddress;

    GivenDelivery(DeliveryService deliveryService, UUID deliveryMethodId, DeliveryAddress deliveryAddress) {
        this.deliveryService = deliveryService;
        this.deliveryMethodId = deliveryMethodId;
        this.deliveryAddress = deliveryAddress;
    }

    public void valid(Price price) {
        givenDeliveryResponseWith(SUCCESS, price);
    }

    public void unsupportedMethod() {
        givenDeliveryResponseWith(UNSUPPORTED_METHOD, NO_PRICE);
    }

    public void notExistingAddress() {
        givenDeliveryResponseWith(NOT_EXISTING_ADDRESS, NO_PRICE);
    }

    private void givenDeliveryResponseWith(DeliveryStatusCode deliveryStatusCode, Price price) {
        DeliveryRequest deliveryRequest = new DeliveryRequest(deliveryMethodId, deliveryAddress);
        DeliveryResponse deliveryResponse = new DeliveryResponse(deliveryStatusCode, price);

        given(deliveryService.calculate(deliveryRequest)).willReturn(deliveryResponse);
    }
}
