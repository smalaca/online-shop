package com.smalaca.purchase.application.cart;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.cart.ProductId;
import org.springframework.stereotype.Service;

@Service
@ApplicationService
public class CartApplicationService {
    private final CartRepository cartRepository;

    public CartApplicationService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @PrimaryAdapter
    public void addProduct(AddProductCommand command) {
        ProductId productId = ProductId.from(command.productId());
        Cart cart = cartRepository.findBy(CartId.from(command.cartId()));

        cart.addProduct(productId);

        cartRepository.save(cart);
    }

    @PrimaryAdapter
    public void removeProduct(RemoveProductCommand command) {
        ProductId productId = ProductId.from(command.productId());
        Cart cart = cartRepository.findBy(CartId.from(command.cartId()));

        cart.removeProduct(productId);

        cartRepository.save(cart);
    }
}
