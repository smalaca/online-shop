package com.smalaca.purchase.application.offer;

import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.deliveryservice.GivenDeliveryFactory;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferFactory;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.order.OrderFactory;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.productmanagementservice.GivenAvailability;
import com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProduct;
import com.smalaca.purchase.domain.selection.Selection;
import com.smalaca.purchase.domain.selection.SelectionTestFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.smalaca.purchase.domain.quantitativeproduct.QuantitativeProductTestFactory.quantitativeProduct;
import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.given;

class GivenOffer {
    private final OfferRepository offerRepository;
    private final OfferFactory offerFactory;
    private final GivenDeliveryFactory givenDelivery;
    private final GivenAvailability givenAvailability;

    private final List<QuantitativeProduct> quantitativeProducts = new ArrayList<>();
    private UUID offerId;
    private UUID buyerId;
    private UUID deliveryMethodId;
    private DeliveryAddress deliveryAddress;
    private OrderFactory orderFactory;


    GivenOffer(
            OfferRepository offerRepository, OfferFactory offerFactory,
            GivenDeliveryFactory givenDelivery, GivenAvailability givenAvailability, OrderFactory orderFactory) {
        this.offerRepository = offerRepository;
        this.offerFactory = offerFactory;
        this.givenDelivery = givenDelivery;
        this.givenAvailability = givenAvailability;
        this.orderFactory = orderFactory;
    }

    GivenOffer withOfferId(UUID offerId) {
        this.offerId = offerId;
        return this;
    }

    void created() {
        Offer offer = offer();
        given(offerRepository.findById(offerId)).willReturn(offer);
    }

    void accepted() {
        Offer offer = offer();
        givenAvailability.forReserving(buyerId, quantitativeProducts);
        offer.accept(orderFactory);

        given(offerRepository.findById(offerId)).willReturn(offer);
    }

    private Offer offer() {
        givenAvailability.forChecking();
        return offerWith(offerId);
    }

    private Offer offerWith(UUID offerId) {
        return offerWith(offerId, offerFactory.create(command()));
    }

    private ChooseProductsDomainCommand command() {
        return new ChooseProductsDomainCommand(buyerId, selections(), deliveryMethodId, deliveryAddress);
    }

    private List<Selection> selections() {
        return quantitativeProducts.stream()
                .map(product -> SelectionTestFactory.selection(product.getProductId(), product.getQuantity().getValue()))
                .collect(toList());
    }

    private Offer offerWith(UUID offerId, Offer offer) {
        try {
            Field offerIdField = offer.getClass().getDeclaredField("offerId");
            offerIdField.setAccessible(true);
            offerIdField.set(offer, offerId);

            return offer;
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    GivenOffer withBuyerId(UUID buyerId) {
        this.buyerId = buyerId;
        return this;
    }

    GivenOffer withDelivery(UUID deliveryMethodId, DeliveryAddress deliveryAddress, Price price) {
        this.deliveryMethodId = deliveryMethodId;
        this.deliveryAddress = deliveryAddress;
        givenDelivery.forRequest(deliveryMethodId, deliveryAddress).valid(price);

        return this;
    }

    GivenOffer withProduct(UUID sellerId, UUID productId, int quantity, BigDecimal price) {
        givenAvailability.available(sellerId, productId, quantity, price);
        quantitativeProducts.add(quantitativeProduct(sellerId, productId, quantity, price));

        return this;
    }
}
