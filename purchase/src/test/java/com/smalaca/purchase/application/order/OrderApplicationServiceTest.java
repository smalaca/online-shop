package com.smalaca.purchase.application.order;

import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.order.OrderRepository;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.purchase.Purchase;
import com.smalaca.purchase.domain.purchase.PurchaseAssertion;
import com.smalaca.purchase.domain.purchase.PurchaseRepository;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static com.smalaca.purchase.domain.purchase.PurchaseAssertion.assertPurchase;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

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
    private static final LocalDateTime PURCHASE_CREATION_DATE_TIME = LocalDateTime.of(LocalDate.of(2023, 9, 27), LocalTime.now());
    private static final BigDecimal PRICE_ONE = randomPrice();
    private static final BigDecimal PRICE_TWO = randomPrice();
    private static final BigDecimal PRICE_THREE = randomPrice();
    private static final BigDecimal TOTAL_PRICE = randomPrice();
    private static final UUID SELLER_ONE = randomId();
    private static final UUID SELLER_TWO = randomId();
    protected static final int QUANTITY_ONE = 2;
    protected static final int QUANTITY_TWO = 8;
    protected static final int QUANTITY_THREE = 4;

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
    void shouldCreatePurchaseWhenPurchasingOrder() {
        givenOrder().created();

        service.purchase(ORDER_ID, PAYMENT_METHOD_ID);

        thenSavedPurchase()
                .hasPurchaseNumberThatStartsWith("Purchase/" + BUYER_ID + "/2023/09/27/")
                .hasOrderId(ORDER_ID)
                .hasBuyerId(BUYER_ID)
                .hasCreationDateTime(PURCHASE_CREATION_DATE_TIME)
                .hasPaymentMethod(PAYMENT_METHOD_ID)
//                .hasTotalPrice(TOTAL_PRICE)
        ;
    }

    private GivenOrder givenOrder() {
        return givenOrder
                .createdAt(ORDER_CREATION_DATE_TIME)
                .withBuyerId(BUYER_ID)
                .withOfferId(OFFER_ID)
                .withOrderId(ORDER_ID)
                .withDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .withProduct(SELLER_ONE, PRODUCT_ID_ONE, QUANTITY_ONE, PRICE_ONE)
                .withProduct(SELLER_ONE, PRODUCT_ID_TWO, QUANTITY_TWO, PRICE_TWO)
                .withProduct(SELLER_TWO, PRODUCT_ID_THREE, QUANTITY_THREE, PRICE_THREE);
    }

    private PurchaseAssertion thenSavedPurchase() {
        ArgumentCaptor<Purchase> captor = ArgumentCaptor.forClass(Purchase.class);
        then(purchaseRepository).should().save(captor.capture());

        return assertPurchase(captor.getValue());
    }

    private static BigDecimal randomPrice() {
        return BigDecimal.valueOf(FAKER.number().numberBetween(1, 1000));
    }

    private static DeliveryAddress randomDeliveryAddress() {
        Address address = FAKER.address();
        return new DeliveryAddress(address.streetAddress(), address.city(), address.postcode(), address.country());
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }
}