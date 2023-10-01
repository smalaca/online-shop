package com.smalaca.purchase.application.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderFactory;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ApplicationService
public class OfferApplicationService {
    private final OfferRepository offerRepository;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;

    private OfferApplicationService(OfferRepository offerRepository, OrderRepository orderRepository, OrderFactory orderFactory) {
        this.offerRepository = offerRepository;
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
    }

    public static OfferApplicationService create(
            OfferRepository offerRepository, OrderRepository orderRepository, ProductManagementService productManagementService, Clock clock) {
        OrderFactory orderFactory = new OrderFactory(productManagementService, clock);
        return new OfferApplicationService(offerRepository, orderRepository, orderFactory);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void accept(UUID buyerId, UUID offerId) {
        Offer offer = offerRepository.findById(offerId);

        Order order = offer.accept(buyerId, orderFactory);

        orderRepository.save(order);
        offerRepository.save(offer);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void reject(UUID offerId) {
        Offer offer = offerRepository.findById(offerId);

        offer.reject();

        offerRepository.save(offer);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void recreate(UUID offerId) {
        Offer offer = offerRepository.findById(offerId);

        Offer recreated = offer.recreate();

        offerRepository.save(offer);
        offerRepository.save(recreated);
    }
}
