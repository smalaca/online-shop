package com.smalaca.purchase.application.offer;

import com.google.common.collect.ImmutableList;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.order.Order;
import com.smalaca.purchase.domain.order.OrderAssertion;
import com.smalaca.purchase.domain.order.OrderExceptionAssertion;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.product.Product;
import com.smalaca.purchase.domain.productmanagementservice.GivenAvailabilityFactory;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.smalaca.purchase.domain.order.OrderAssertion.assertOrder;
import static com.smalaca.purchase.domain.order.OrderExceptionAssertion.assertOrderProductsException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

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
    private static final LocalDateTime OFFER_CREATION_DATE_TIME = LocalDateTime.of(LocalDate.of(2023, 9, 25), LocalTime.now());
    private static final BigDecimal PRICE_ONE = randomPrice();
    private static final BigDecimal PRICE_TWO = randomPrice();
    private static final BigDecimal PRICE_THREE = randomPrice();
    private static final UUID SELLER_ONE = randomId();
    private static final UUID SELLER_TWO = randomId();
    protected static final int AMOUNT_ONE = 2;
    protected static final int AMOUNT_TWO = 8;
    protected static final int AMOUNT_THREE = 4;
    private static final List<Product> PRODUCTS = ImmutableList.of(
            Product.product(PRODUCT_ID_ONE, AMOUNT_ONE),
            Product.product(PRODUCT_ID_TWO, AMOUNT_TWO),
            Product.product(PRODUCT_ID_THREE, AMOUNT_THREE));

    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final ProductManagementService productManagementService = mock(ProductManagementService.class);
    private final OfferApplicationService service = OfferApplicationService.create(offerRepository, orderRepository, productManagementService);

    private final GivenAvailabilityFactory givenAvailability = new GivenAvailabilityFactory(productManagementService);

    @BeforeEach
    void givenOffer() {
        GivenOfferFactory.create(offerRepository)
                .createdAt(OFFER_CREATION_DATE_TIME)
                .withBuyerId(BUYER_ID)
                .withDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .withProduct(SELLER_ONE, PRODUCT_ID_ONE, AMOUNT_ONE, PRICE_ONE)
                .withProduct(SELLER_ONE, PRODUCT_ID_TWO, AMOUNT_TWO, PRICE_TWO)
                .withProduct(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE)
                .withId(OFFER_ID);
    }

    @Test
    void shouldRecognizeProductIsNotAvailableAnymoreWhenAccepting() {
        givenAvailability
                .notAvailable(PRODUCT_ID_ONE)
                .notAvailable(PRODUCT_ID_TWO)
                .available(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE)
                .forReserving(BUYER_ID, PRODUCTS);

        Executable executable = () -> service.accept(BUYER_ID, OFFER_ID);

        thenOrderNotCreatedDueToOrderExceptionThat(executable)
                .hasMessage("Cannot create Order because products are not available anymore.")
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, AMOUNT_ONE)
                .containsProduct(PRODUCT_ID_TWO, AMOUNT_TWO);
    }

    private OrderExceptionAssertion thenOrderNotCreatedDueToOrderExceptionThat(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenOfferNotSaved();
        thenOrderNotSaved();

        return assertOrderProductsException(actual);

    }

    private void thenOfferNotSaved() {
        then(offerRepository).should(never()).save(any());
    }

    private void thenOrderNotSaved() {
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void shouldAcceptOffer() {
        givenAvailability
                .available(SELLER_ONE, PRODUCT_ID_ONE, AMOUNT_ONE, PRICE_ONE)
                .available(SELLER_ONE, PRODUCT_ID_TWO, AMOUNT_TWO, PRICE_TWO)
                .available(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE)
                .forReserving(BUYER_ID, PRODUCTS);

        service.accept(BUYER_ID, OFFER_ID);

        thenSavedOrder()
                .hasOfferId(OFFER_ID)
                .hasDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .hasProducts(3)
                .containsProduct(SELLER_ONE, PRODUCT_ID_ONE, AMOUNT_ONE, PRICE_ONE)
                .containsProduct(SELLER_ONE, PRODUCT_ID_TWO, AMOUNT_TWO, PRICE_TWO)
                .containsProduct(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE);
    }

    @Test
    void shouldAcceptOfferWithUpdatedPrices() {
        BigDecimal newPriceOne = randomPrice();
        BigDecimal newPriceTwo = randomPrice();
        givenAvailability
                .available(SELLER_ONE, PRODUCT_ID_ONE, AMOUNT_ONE, newPriceOne)
                .available(SELLER_ONE, PRODUCT_ID_TWO, AMOUNT_TWO, newPriceTwo)
                .available(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE)
                .forReserving(BUYER_ID, PRODUCTS);

        service.accept(BUYER_ID, OFFER_ID);

        thenSavedOrder()
                .hasOfferId(OFFER_ID)
                .hasDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .hasProducts(3)
                .containsProduct(SELLER_ONE, PRODUCT_ID_ONE, AMOUNT_ONE, newPriceOne)
                .containsProduct(SELLER_ONE, PRODUCT_ID_TWO, AMOUNT_TWO, newPriceTwo)
                .containsProduct(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE);
    }

    private static BigDecimal randomPrice() {
        return BigDecimal.valueOf(FAKER.number().numberBetween(1, 1000));
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