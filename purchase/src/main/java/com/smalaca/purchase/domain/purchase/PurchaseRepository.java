package com.smalaca.purchase.domain.purchase;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.annotations.ddd.Repository;

@Repository
@SecondaryPort
public interface PurchaseRepository {
    void save(Purchase purchase);
}
