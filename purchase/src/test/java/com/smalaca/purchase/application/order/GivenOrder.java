package com.smalaca.purchase.application.order;

import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.order.AcceptOfferDomainCommand;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderFactory;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.productmanagementservice.GivenAvailability;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;

class GivenOrder {
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final GivenAvailability givenAvailability;

    private final List<Product> products = new ArrayList<>();
    private UUID buyerId;
    private UUID offerId;
    private UUID orderId;
    private Delivery delivery;

    GivenOrder(OrderRepository orderRepository, OrderFactory orderFactory, GivenAvailability givenAvailability) {
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        this.givenAvailability = givenAvailability;
    }

    GivenOrder withBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    GivenOrder withOfferId(UUID offerId) {
        this.offerId = offerId;
        return this;
    }

    GivenOrder withOrderId(UUID orderId) {
        this.orderId = orderId;
        return this;
    }

    GivenOrder withDelivery(UUID deliveryMethodId, DeliveryAddress deliveryAddress, Price deliveryPrice) {
        delivery = new Delivery(deliveryMethodId, deliveryAddress, deliveryPrice);
        return this;
    }

    GivenOrder withProduct(UUID sellerId, UUID productId, int amount, BigDecimal price) {
        givenAvailability.available(sellerId, productId, amount, price);
        products.add(Product.product(productId, amount));
        return this;
    }

    void created() {
        Order order = order();
        given(orderRepository.findById(orderId)).willReturn(order);
    }

    private Order order() {
        givenAvailability.forReserving(buyerId, products);
        Order order = orderFactory.create(command());

        return orderWithId(order);
    }

    private AcceptOfferDomainCommand command() {
        return new AcceptOfferDomainCommand(buyerId, offerId, delivery, products);
    }

    private Order orderWithId(Order order) {
        try {
            Field offerIdField = order.getClass().getDeclaredField("orderId");
            offerIdField.setAccessible(true);
            offerIdField.set(order, orderId);

            return order;
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
}
