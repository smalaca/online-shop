package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.annotations.ddd.Repository;

import java.util.UUID;

@Repository
@SecondaryPort
public interface CartRepository {
    Cart findBy(UUID cartId);

    void save(Cart cart);
}
