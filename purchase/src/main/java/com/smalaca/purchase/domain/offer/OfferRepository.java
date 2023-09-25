package com.smalaca.purchase.domain.offer;

import com.smalaca.annotations.architectures.portadapter.SecondaryPort;
import com.smalaca.annotations.ddd.Repository;

import java.util.UUID;

@Repository
@SecondaryPort
public interface OfferRepository {
    void save(Offer offer);

    Offer findById(UUID offerId);
}
