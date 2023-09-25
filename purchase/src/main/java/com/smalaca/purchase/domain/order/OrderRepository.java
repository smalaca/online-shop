package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.annotations.ddd.Repository;

import java.util.UUID;

@SecondaryPort
@Repository
public interface OrderRepository {
    void save(Order order);

    Order findById(UUID orderId);
}
