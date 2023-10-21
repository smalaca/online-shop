package com.smalaca.purchase.application.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.purchase.Purchase;
import com.smalaca.purchase.domain.purchase.PurchaseFactory;
import com.smalaca.purchase.domain.purchase.PurchaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ApplicationService
public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseFactory purchaseFactory;

    private OrderApplicationService(
            OrderRepository orderRepository, PurchaseRepository purchaseRepository, PurchaseFactory purchaseFactory) {
        this.orderRepository = orderRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseFactory = purchaseFactory;
    }

    static OrderApplicationService create(OrderRepository orderRepository, PurchaseRepository purchaseRepository, Clock clock) {
        return new OrderApplicationService(orderRepository, purchaseRepository, new PurchaseFactory(clock));
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void purchase(UUID orderId, UUID paymentMethodId) {
        Order order = orderRepository.findById(orderId);

        Purchase purchase = order.purchase(paymentMethodId, purchaseFactory);

        purchaseRepository.save(purchase);
    }
}
