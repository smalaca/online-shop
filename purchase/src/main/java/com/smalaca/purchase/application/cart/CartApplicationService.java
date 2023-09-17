package com.smalaca.purchase.application.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.cart.Product;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@ApplicationService
public class CartApplicationService {
    private final CartRepository cartRepository;
    private final OfferRepository offerRepository;

    public CartApplicationService(CartRepository cartRepository, OfferRepository offerRepository) {
        this.cartRepository = cartRepository;
        this.offerRepository = offerRepository;
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void addProduct(CartProductsDto dto) {
        List<Product> products = dto.asProducts();
        Cart cart = cartRepository.findBy(new CartId(dto.cartId()));

        cart.add(products);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void removeProduct(CartProductsDto dto) {
        Cart cart = cartRepository.findBy(new CartId(dto.cartId()));

        cart.removeProduct(dto.asProducts());

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void chooseProducts(CartProductsDto dto) {
        List<Product> products = dto.asProducts();
        Cart cart = cartRepository.findBy(new CartId(dto.cartId()));

        Offer offer = cart.choose(products);

        offerRepository.save(offer);
    }
}
