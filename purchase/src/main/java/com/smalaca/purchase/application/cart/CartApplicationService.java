package com.smalaca.purchase.application.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.offer.Clock;
import com.smalaca.purchase.domain.offer.DeliveryService;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferFactory;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.offer.ProductManagementService;
import com.smalaca.purchase.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ApplicationService
public class CartApplicationService {
    private final CartRepository cartRepository;
    private final OfferRepository offerRepository;
    private final ProductManagementService productManagementService;
    private final OfferFactory offerFactory;

    private CartApplicationService(
            CartRepository cartRepository, OfferRepository offerRepository, ProductManagementService productManagementService, OfferFactory offerFactory) {
        this.cartRepository = cartRepository;
        this.offerRepository = offerRepository;
        this.productManagementService = productManagementService;
        this.offerFactory = offerFactory;
    }

    static CartApplicationService create(
            CartRepository cartRepository, OfferRepository offerRepository, ProductManagementService productManagementService,
            DeliveryService deliveryService, Clock clock) {
        OfferFactory offerFactory = new OfferFactory(productManagementService, deliveryService, clock);
        return new CartApplicationService(cartRepository, offerRepository, productManagementService, offerFactory);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void addProducts(AddProductsCommand command) {
        List<Product> products = command.asProducts();
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        cart.add(products);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void removeProducts(RemoveProductsCommand command) {
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        cart.remove(command.asProducts());

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void chooseProducts(ChooseProductsCommand command) {
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        Offer offer = cart.choose(command.asCommand(), offerFactory);

        offerRepository.save(offer);
    }
}
