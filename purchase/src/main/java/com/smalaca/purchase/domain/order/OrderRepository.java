package com.smalaca.purchase.domain.order;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.annotations.ddd.Repository;

@SecondaryPort
@Repository
public interface OrderRepository {
    void save(Order order);
}
