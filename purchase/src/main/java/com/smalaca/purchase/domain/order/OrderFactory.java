package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.ddd.Factory;
import com.smalaca.purchase.domain.productmanagementservice.AvailableProduct;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;

import java.util.List;

@Factory
public class OrderFactory {
    private final ProductManagementService productManagementService;

    public OrderFactory(ProductManagementService productManagementService) {
        this.productManagementService = productManagementService;
    }

    public Order create(AcceptOfferDomainCommand command) {
        Order.Builder builder = new Order.Builder()
                .offerId(command.offerId())
                .delivery(command.delivery());

        List<AvailableProduct> availableProducts = productManagementService.reserve(command.buyerId(), command.products());

        availableProducts.forEach(builder::item);

        return builder.build();
    }
}
