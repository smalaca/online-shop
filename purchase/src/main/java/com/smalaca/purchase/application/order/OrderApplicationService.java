package com.smalaca.purchase.application.order;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderId;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.purchase.Purchase;
import com.smalaca.purchase.domain.purchase.PurchaseRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ApplicationService
public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final PurchaseRepository purchaseRepository;

    public OrderApplicationService(OrderRepository orderRepository, PurchaseRepository purchaseRepository) {
        this.orderRepository = orderRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void reject(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId));

        order.reject();

        orderRepository.save(order);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void purchase(UUID orderId) {
        Order order = orderRepository.findById(new OrderId(orderId));

        Purchase purchase = order.purchase();

        purchaseRepository.save(purchase);
    }
}
