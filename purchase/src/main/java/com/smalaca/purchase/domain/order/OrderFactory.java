package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import com.smalaca.purchase.domain.productmanagementservice.ProductsReservation;

import java.util.List;
import java.util.UUID;

@Factory
public class OrderFactory {
    private final ProductManagementService productManagementService;

    public OrderFactory(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    public Order create(AcceptOfferDomainCommand command) {
        Order.Builder builder = new Order.Builder()
                .offerId(command.offerId())
                .buyerId(command.buyerId())
                .delivery(command.delivery());

        ProductsReservation productsReservation = productManagementService.reserve(command.buyerId(), command.products());

        if (productsReservation.wasNotReserved()) {
            throw OrderException.notAvailableProducts(notAvailableProducts(command, productsReservation));
        }

        productsReservation.reservedProducts().forEach(builder::item);

        return builder.build();
    }

    private List<Product> notAvailableProducts(AcceptOfferDomainCommand command, ProductsReservation productsReservation) {
        List<UUID> missingProducts = productsReservation.missingProducts();

        return command.products().stream()
                .filter(product -> missingProducts.contains(product.getProductId()))
                .toList();
    }
}
