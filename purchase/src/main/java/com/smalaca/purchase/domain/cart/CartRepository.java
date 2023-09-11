package com.smalaca.purchase.domain.cart;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.annotations.ddd.Repository;

@Repository
@SecondaryPort
public interface CartRepository {
    Cart findBy(CartId from);

    void save(Cart cart);
}
