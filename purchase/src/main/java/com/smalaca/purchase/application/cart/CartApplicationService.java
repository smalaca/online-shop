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

import static java.util.stream.Collectors.toList;

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
    public void addProduct(AddProductsCommand command) {
        List<Product> products = command.asProducts();
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        cart.add(products);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void removeProduct(RemoveProductCommand command) {
        Product product = Product.product(command.productId(), 0);
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        cart.removeProduct(product);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void chooseProducts(ChooseProductsCommand command) {
        List<Product> products = asProductsIds(command);
        Cart cart = cartRepository.findBy(new CartId(command.cartId()));

        Offer offer = cart.choose(products);

        offerRepository.save(offer);
    }

    private List<Product> asProductsIds(ChooseProductsCommand command) {
        return command.productsIds().stream()
                .map(productId -> Product.product(productId, 0))
                .collect(toList());
    }
}
