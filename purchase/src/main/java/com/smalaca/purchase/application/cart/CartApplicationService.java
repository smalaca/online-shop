package com.smalaca.purchase.application.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.productid.ProductId;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferRepository;
import org.springframework.stereotype.Service;

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
    public void addProduct(AddProductCommand command) {
        ProductId productId = ProductId.from(command.productId());
        Cart cart = cartRepository.findBy(CartId.from(command.cartId()));

        cart.addProduct(productId);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    public void removeProduct(RemoveProductCommand command) {
        ProductId productId = ProductId.from(command.productId());
        Cart cart = cartRepository.findBy(CartId.from(command.cartId()));

        cart.removeProduct(productId);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    @Command
    public void chooseProducts(ChooseProductsCommand command) {
        List<ProductId> productsIds = asProductsIds(command);
        Cart cart = cartRepository.findBy(CartId.from(command.cartId()));

        Offer offer = cart.choose(productsIds);

        offerRepository.save(offer);
    }

    private List<ProductId> asProductsIds(ChooseProductsCommand command) {
        return command.productsIds().stream()
                .map(ProductId::from)
                .collect(toList());
    }
}
