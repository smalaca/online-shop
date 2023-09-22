package com.smalaca.purchase.application.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.offer.Clock;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.offer.ProductManagementService;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferFactory;
import com.smalaca.purchase.domain.offer.OfferRepository;
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

    static CartApplicationService create(CartRepository cartRepository, OfferRepository offerRepository, ProductManagementService productManagementService, Clock clock) {
        return new CartApplicationService(cartRepository, offerRepository, productManagementService, new OfferFactory(productManagementService, clock));
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void addProduct(AddProductCommand command) {
        List<Product> products = command.asProducts();
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        cart.add(products);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void removeProduct(CartProductsDto dto) {
        Cart cart = cartRepository.findBy(new CartId(dto.cartId()));

        cart.remove(dto.asProducts());

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void chooseProducts(CartProductsDto dto) {
        Cart cart = cartRepository.findBy(new CartId(dto.cartId()));

        Offer offer = cart.choose(dto.asProducts(), offerFactory);

        offerRepository.save(offer);
    }
}
