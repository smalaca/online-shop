package com.smalaca.purchase.application.offer;

import com.smalaca.annotations.architectures.portadapter.PrimaryAdapter;
import com.smalaca.annotations.ddd.ApplicationService;
import com.smalaca.annotations.patterns.cqrs.Command;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferId;
import com.smalaca.purchase.domain.offer.OfferRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ApplicationService
public class OfferApplicationService {
    private final OfferRepository offerRepository;

    public OfferApplicationService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void reject(UUID offerId) {
        Offer offer = offerRepository.findById(OfferId.from(offerId));

        offer.reject();

        offerRepository.save(offer);
    }

    @PrimaryAdapter
    @Command
    @Transactional
    public void recreate(UUID offerId) {
        Offer offer = offerRepository.findById(OfferId.from(offerId));

        Offer recreated = offer.recreate();

        offerRepository.save(offer);
        offerRepository.save(recreated);
    }
}
