package com.smalaca.purchase.application.offer;

import com.google.common.collect.ImmutableList;
import com.smalaca.purchase.application.cart.GivenAvailabilityFactory;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.deliveryservice.DeliveryService;
import com.smalaca.purchase.domain.deliveryservice.GivenDeliveryFactory;
import com.smalaca.purchase.domain.offer.ChooseProductsDomainCommand;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferFactory;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderAssertion;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.smalaca.purchase.domain.order.OrderAssertion.assertOrder;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class OfferApplicationServiceTest {
    private static final Faker FAKER = new Faker();
    private static final UUID OFFER_ID = randomId();
    private static final UUID BUYER_ID = randomId();
    private static final UUID DELIVERY_METHOD_ID = randomId();
    private static final DeliveryAddress DELIVERY_ADDRESS = randomDeliveryAddress();
    private static final Price DELIVERY_PRICE = Price.price(BigDecimal.valueOf(234.53));
    private static final UUID PRODUCT_ID_ONE = randomId();
    private static final UUID PRODUCT_ID_TWO = randomId();
    private static final UUID PRODUCT_ID_THREE = randomId();
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(LocalDate.of(2023, 9, 25), LocalTime.now());
    private static final BigDecimal PRICE_ONE = BigDecimal.valueOf(13.11);
    private static final BigDecimal PRICE_TWO = BigDecimal.valueOf(43.223);
    private static final BigDecimal PRICE_THREE = BigDecimal.valueOf(123.23);
    private static final UUID SELLER_ONE = randomId();
    private static final UUID SELLER_TWO = randomId();

    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OfferApplicationService service = new OfferApplicationService(offerRepository, orderRepository);

    private final DeliveryService deliveryService = mock(DeliveryService.class);
    private final GivenDeliveryFactory givenDelivery = new GivenDeliveryFactory(deliveryService);

    @Test
    void shouldAcceptOffer() {
        ProductManagementService productManagementService = mock(ProductManagementService.class);
        Clock clock = mock(Clock.class);
        OfferFactory offerFactory = new OfferFactory(productManagementService, deliveryService, clock);
        List<Product> products = ImmutableList.of(
                Product.product(PRODUCT_ID_ONE, 2),
                Product.product(PRODUCT_ID_TWO, 7),
                Product.product(PRODUCT_ID_THREE, 3));
        ChooseProductsDomainCommand command = new ChooseProductsDomainCommand(BUYER_ID, products, DELIVERY_METHOD_ID, DELIVERY_ADDRESS);
        givenDelivery.forRequest(DELIVERY_METHOD_ID, DELIVERY_ADDRESS).valid(DELIVERY_PRICE);
        given(clock.nowDateTime()).willReturn(CREATED_AT);
        new GivenAvailabilityFactory(productManagementService)
                .available(SELLER_ONE, PRODUCT_ID_ONE, 2, PRICE_ONE)
                .available(SELLER_ONE, PRODUCT_ID_TWO, 8, PRICE_TWO)
                .available(SELLER_TWO, PRODUCT_ID_THREE, 4, PRICE_THREE)
                .set();
        Offer offer = offerFactory.create(command);
        try {
            Field offerId = offer.getClass().getDeclaredField("offerId");
            offerId.setAccessible(true);
            offerId.set(offer, OFFER_ID);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        given(offerRepository.findById(OFFER_ID)).willReturn(offer);

        service.accept(OFFER_ID);

        thenSavedOrder()
                .hasOfferId(OFFER_ID)
                .hasDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .hasProducts(3)
                .containsProduct(SELLER_ONE, PRODUCT_ID_ONE, 2, PRICE_ONE)
                .containsProduct(SELLER_ONE, PRODUCT_ID_TWO, 7, PRICE_TWO)
                .containsProduct(SELLER_TWO, PRODUCT_ID_THREE, 3, PRICE_THREE);
    }

    private OrderAssertion thenSavedOrder() {
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        then(orderRepository).should().save(captor.capture());

        return assertOrder(captor.getValue());
    }

    private static DeliveryAddress randomDeliveryAddress() {
        Address address = FAKER.address();
        return new DeliveryAddress(address.streetAddress(), address.city(), address.postcode(), address.country());
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }
}