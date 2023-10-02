package com.smalaca.purchase.application.offer;

import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryservice.DeliveryService;
import com.smalaca.purchase.domain.deliveryservice.GivenDeliveryFactory;
import com.smalaca.purchase.domain.offer.OfferFactory;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.order.OrderFactory;
import com.smalaca.purchase.domain.productmanagementservice.GivenAvailabilityFactory;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GivenOfferFactory {
    private final OfferRepository offerRepository;
    private final OfferFactory offerFactory;
    private final OrderFactory orderFactory;
    private final Clock clock;
    private final GivenDeliveryFactory givenDeliveryFactory;
    private final GivenAvailabilityFactory givenAvailabilityFactory;

    private GivenOfferFactory(
            OfferRepository offerRepository, OfferFactory offerFactory, OrderFactory orderFactory, Clock clock,
            GivenDeliveryFactory givenDeliveryFactory, GivenAvailabilityFactory givenAvailabilityFactory) {
        this.offerRepository = offerRepository;
        this.offerFactory = offerFactory;
        this.orderFactory = orderFactory;
        this.clock = clock;
        this.givenDeliveryFactory = givenDeliveryFactory;
        this.givenAvailabilityFactory = givenAvailabilityFactory;
    }

    static GivenOfferFactory create(OfferRepository offerRepository) {
        Clock clock = mock(Clock.class);
        ProductManagementService productManagementService = mock(ProductManagementService.class);
        DeliveryService deliveryService = mock(DeliveryService.class);

        OfferFactory offerFactory = new OfferFactory(productManagementService, deliveryService, clock);
        OrderFactory orderFactory = new OrderFactory(productManagementService, clock);
        GivenDeliveryFactory givenDeliveryFactory = new GivenDeliveryFactory(deliveryService);
        GivenAvailabilityFactory givenAvailabilityFactory = new GivenAvailabilityFactory(productManagementService);

        return new GivenOfferFactory(
                offerRepository, offerFactory, orderFactory, clock, givenDeliveryFactory, givenAvailabilityFactory);
    }

    GivenOffer createdAt(LocalDateTime creationDateTime) {
        given(clock.nowDateTime()).willReturn(creationDateTime);

        return new GivenOffer(offerRepository, offerFactory, givenDeliveryFactory, givenAvailabilityFactory.create(), orderFactory);
    }
}
