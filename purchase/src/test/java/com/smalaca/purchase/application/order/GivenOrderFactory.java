package com.smalaca.purchase.application.order;

import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.order.OrderFactory;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.productmanagementservice.GivenAvailabilityFactory;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class GivenOrderFactory {
    private final OrderRepository orderRepository;
    private final Clock clock;
    private final OrderFactory orderFactory;
    private final GivenAvailabilityFactory givenAvailabilityFactory;

    private GivenOrderFactory(
            OrderRepository orderRepository, Clock clock, OrderFactory orderFactory,
            GivenAvailabilityFactory givenAvailabilityFactory) {
        this.orderRepository = orderRepository;
        this.clock = clock;
        this.orderFactory = orderFactory;
        this.givenAvailabilityFactory = givenAvailabilityFactory;
    }

    static GivenOrderFactory create(OrderRepository orderRepository) {
        ProductManagementService productManagementService = mock(ProductManagementService.class);
        Clock clock = mock(Clock.class);
        OrderFactory orderFactory = new OrderFactory(productManagementService, clock);
        GivenAvailabilityFactory givenAvailabilityFactory = new GivenAvailabilityFactory(productManagementService);
        return new GivenOrderFactory(orderRepository, clock, orderFactory, givenAvailabilityFactory);
    }

    GivenOrder createdAt(LocalDateTime creationDateTime) {
        given(clock.nowDateTime()).willReturn(creationDateTime);
        return new GivenOrder(orderRepository, orderFactory, givenAvailabilityFactory.create());
    }
}
