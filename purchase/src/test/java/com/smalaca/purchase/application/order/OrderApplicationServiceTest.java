package com.smalaca.purchase.application.order;

import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.order.OrderExceptionAssertion;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.purchase.Purchase;
import com.smalaca.purchase.domain.purchase.PurchaseAssertion;
import com.smalaca.purchase.domain.purchase.PurchaseRepository;
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
import java.util.UUID;

import static com.smalaca.purchase.domain.order.OrderExceptionAssertion.assertOrderProductsException;
import static com.smalaca.purchase.domain.purchase.PurchaseAssertion.assertPurchase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class OrderApplicationServiceTest {
    private static final Faker FAKER = new Faker();

    private static final UUID OFFER_ID = randomId();
    private static final UUID ORDER_ID = randomId();
    private static final UUID BUYER_ID = randomId();
    private static final UUID PAYMENT_METHOD_ID = randomId();
    private static final UUID DELIVERY_METHOD_ID = randomId();
    private static final DeliveryAddress DELIVERY_ADDRESS = randomDeliveryAddress();
    private static final Price DELIVERY_PRICE = Price.price(BigDecimal.valueOf(234.53));
    private static final UUID PRODUCT_ID_ONE = randomId();
    private static final UUID PRODUCT_ID_TWO = randomId();
    private static final UUID PRODUCT_ID_THREE = randomId();
    private static final LocalDateTime ORDER_CREATION_DATE_TIME = LocalDateTime.of(LocalDate.of(2023, 9, 26), LocalTime.now());
    private static final LocalDateTime PURCHASE_CREATION_DATE_TIME = ORDER_CREATION_DATE_TIME.plusMinutes(2);
    private static final BigDecimal PRICE_ONE = BigDecimal.valueOf(123.32);
    private static final BigDecimal PRICE_TWO = BigDecimal.valueOf(430.2);
    private static final BigDecimal PRICE_THREE = BigDecimal.valueOf(13);
    private static final UUID SELLER_ONE = randomId();
    private static final UUID SELLER_TWO = randomId();
    private static final int AMOUNT_ONE = 2;
    private static final int AMOUNT_TWO = 8;
    private static final int AMOUNT_THREE = 4;
    private static final Price TOTAL_PRICE = Price.price(BigDecimal.valueOf(3974.77));

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final PurchaseRepository purchaseRepository = mock(PurchaseRepository.class);
    private final Clock clock = mock(Clock.class);
    private final OrderApplicationService service = OrderApplicationService.create(orderRepository, purchaseRepository, clock);

    private final GivenOrderFactory givenOrder = GivenOrderFactory.create(orderRepository);

    @BeforeEach
    void givenNow() {
        given(clock.nowDateTime()).willReturn(PURCHASE_CREATION_DATE_TIME);
    }

    @Test
    void shouldRecognizeExpiredOrder() {
        givenOrder().created();
        given(clock.nowDateTime()).willReturn(ORDER_CREATION_DATE_TIME.plusMinutes(20));

        Executable executable = () -> service.purchase(ORDER_ID, PAYMENT_METHOD_ID);

        thenPurchaseNotMadeDueToOrderException(executable)
                .hasMessage("Order " + ORDER_ID + " expired.");
    }

    private OrderExceptionAssertion thenPurchaseNotMadeDueToOrderException(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenOrderNotSaved();
        thenPurchaseNotSaved();

        return assertOrderProductsException(actual);
    }

    @Test
    void shouldCreatePurchaseWhenPurchasingOrder() {
        givenOrder().created();

        service.purchase(ORDER_ID, PAYMENT_METHOD_ID);

        thenSavedPurchase()
                .hasPurchaseNumberThatStartsWith("Purchase/" + BUYER_ID + "/2023/09/26/")
                .hasOrderId(ORDER_ID)
                .hasBuyerId(BUYER_ID)
                .hasCreationDateTime(PURCHASE_CREATION_DATE_TIME)
                .hasPaymentMethod(PAYMENT_METHOD_ID)
                .hasTotalPrice(TOTAL_PRICE);
    }

    private GivenOrder givenOrder() {
        return givenOrder
                .createdAt(ORDER_CREATION_DATE_TIME)
                .withBuyerId(BUYER_ID)
                .withOfferId(OFFER_ID)
                .withOrderId(ORDER_ID)
                .withDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .withProduct(SELLER_ONE, PRODUCT_ID_ONE, AMOUNT_ONE, PRICE_ONE)
                .withProduct(SELLER_ONE, PRODUCT_ID_TWO, AMOUNT_TWO, PRICE_TWO)
                .withProduct(SELLER_TWO, PRODUCT_ID_THREE, AMOUNT_THREE, PRICE_THREE);
    }

    private PurchaseAssertion thenSavedPurchase() {
        ArgumentCaptor<Purchase> captor = ArgumentCaptor.forClass(Purchase.class);
        then(purchaseRepository).should().save(captor.capture());

        return assertPurchase(captor.getValue());
    }

    private void thenPurchaseNotSaved() {
        then(purchaseRepository).should(never()).save(any());
    }

    private void thenOrderNotSaved() {
        then(orderRepository).should(never()).save(any());
    }

    private static DeliveryAddress randomDeliveryAddress() {
        Address address = FAKER.address();
        return new DeliveryAddress(address.streetAddress(), address.city(), address.postcode(), address.country());
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }
}