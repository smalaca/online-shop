package com.smalaca.purchase.domain.offer;

enum OfferState {
    CREATED(true),
    ACCEPTED(false);

    private final boolean canBeAccepted;

    OfferState(boolean canBeAccepted) {
        this.canBeAccepted = canBeAccepted;
    }

    boolean cannotBeAccepted() {
        return !canBeAccepted;
    }
}
