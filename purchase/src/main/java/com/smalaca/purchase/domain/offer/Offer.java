package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryPort;
import com.smalaca.annotations.ddd.AggregateRoot;
import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;
import com.smalaca.purchase.domain.quantity.Quantity;
import com.smalaca.purchase.domain.delivery.Delivery;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.documentnumber.DocumentNumber;
import com.smalaca.purchase.domain.order.AcceptOfferDomainCommand;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderFactory;
import com.smalaca.purchase.domain.price.Price;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.smalaca.purchase.domain.offer.OfferState.ACCEPTED;
import static com.smalaca.purchase.domain.offer.OfferState.CREATED;
import static java.util.stream.Collectors.toList;

@AggregateRoot
public class Offer {
    private UUID offerId;
    private final LocalDateTime creationDateTime;
    private final List<OfferItem> items;
    private final Delivery delivery;
    private final DocumentNumber documentNumber;
    private final UUID buyerId;
    private OfferState offerState;

    private Offer(Builder builder) {
        this.creationDateTime = builder.creationDateTime;
        this.items = builder.items;
        this.delivery = builder.delivery;
        this.documentNumber = builder.documentNumber;
        this.buyerId = builder.buyerId;
        this.offerState = builder.offerState;
    }

    @PrimaryPort
    @Factory
    public Order accept(OrderFactory orderFactory) {
        if (cannotBeAccepted()) {
            throw OfferException.alreadyAccepted(offerId);
        }

        offerState = ACCEPTED;
        return orderFactory.create(new AcceptOfferDomainCommand(buyerId, offerId, delivery, quantitativeProducts()));
    }

    private boolean cannotBeAccepted() {
        return offerState.cannotBeAccepted();
    }

    private List<QuantitativeProduct> quantitativeProducts() {
        return items.stream()
                .map(OfferItem::asQuantitativeProduct)
                .collect(toList());
    }

    @Factory
    static class Builder {
        private final List<OfferItem> items = new ArrayList<>();
        private final OfferState offerState = CREATED;
        private LocalDateTime creationDateTime;
        private DocumentNumber documentNumber;
        private UUID buyerId;
        private Delivery delivery;

        Offer build() {
            documentNumber = DocumentNumber.offerNumber(buyerId, creationDateTime);
            return new Offer(this);
        }

        Builder creationDateTime(LocalDateTime creationDateTime) {
            this.creationDateTime = creationDateTime;
            return this;
        }

        Builder delivery(UUID deliveryMethodId, DeliveryAddress deliveryAddress, Price deliveryPrice) {
            this.delivery = new Delivery(deliveryMethodId, deliveryAddress, deliveryPrice);
            return this;
        }

        Builder buyerId(UUID buyerId) {
            this.buyerId = buyerId;
            return this;
        }

        void item(UUID sellerId, UUID productId, Quantity quantity, Price price) {
            items.add(new OfferItem(sellerId, productId, quantity, price));
        }
    }
}
