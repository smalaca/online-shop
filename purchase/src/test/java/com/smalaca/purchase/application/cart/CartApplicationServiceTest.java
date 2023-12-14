package com.smalaca.purchase.application.cart;

import com.google.common.collect.ImmutableMap;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartAssertion;
import com.smalaca.purchase.domain.cart.CartProductsExceptionAssertion;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.clock.Clock;
import com.smalaca.purchase.domain.deliveryaddress.DeliveryAddress;
import com.smalaca.purchase.domain.deliveryservice.DeliveryService;
import com.smalaca.purchase.domain.deliveryservice.GivenDelivery;
import com.smalaca.purchase.domain.deliveryservice.GivenDeliveryFactory;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferAssertion;
import com.smalaca.purchase.domain.offer.OfferExceptionAssertion;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.price.Price;
import com.smalaca.purchase.domain.productmanagementservice.GivenAvailabilityFactory;
import com.smalaca.purchase.domain.productmanagementservice.ProductManagementService;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import static com.smalaca.purchase.domain.cart.CartAssertion.assertCart;
import static com.smalaca.purchase.domain.cart.CartProductsExceptionAssertion.assertCartProductsException;
import static com.smalaca.purchase.domain.offer.OfferAssertion.assertOffer;
import static com.smalaca.purchase.domain.offer.OfferExceptionAssertion.assertOfferProductsException;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class CartApplicationServiceTest {
    private static final Faker FAKER = new Faker();

    private static final AddressDto ADDRESS_DTO = randomAddressDto();
    private static final DeliveryAddress DELIVERY_ADDRESS = ADDRESS_DTO.asDeliveryAddress();
    private static final UUID PRODUCT_ID_ONE = randomId();
    private static final UUID PRODUCT_ID_TWO = randomId();
    private static final UUID PRODUCT_ID_THREE = randomId();
    private static final UUID PRODUCT_ID_FOUR = randomId();
    private static final UUID PRODUCT_ID_FIVE = randomId();
    private static final UUID BUYER_ID = randomId();
    private static final UUID CART_ID = randomId();
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(LocalDate.of(2023, 9, 25), LocalTime.now());
    private static final BigDecimal PRICE_ONE = BigDecimal.valueOf(13.11);
    private static final BigDecimal PRICE_TWO = BigDecimal.valueOf(43.223);
    private static final BigDecimal PRICE_THREE = BigDecimal.valueOf(123.23);
    private static final UUID SELLER_ONE = randomId();
    private static final UUID SELLER_TWO = randomId();
    private static final UUID DELIVERY_METHOD_ID = randomId();
    private static final Price DELIVERY_PRICE = Price.price(BigDecimal.valueOf(234.53));

    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ProductManagementService productManagementService = mock(ProductManagementService.class);
    private final DeliveryService deliveryService = mock(DeliveryService.class);
    private final Clock clock = mock(Clock.class);
    private final CartApplicationService service = CartApplicationService.create(
            cartRepository, offerRepository, productManagementService, deliveryService, clock);

    private final GivenCartFactory givenCart = new GivenCartFactory(cartRepository);
    private final GivenAvailabilityFactory givenAvailability = new GivenAvailabilityFactory(productManagementService);
    private final GivenDeliveryFactory givenDelivery = new GivenDeliveryFactory(deliveryService);

    @Test
    void shouldAddNothingWhenNothingGiven() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 12)
                .withProduct(PRODUCT_ID_TWO, 4)
                .with(CART_ID);

        service.addProducts(addProductCommand(emptyMap()));

        thenSavedCart()
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 12)
                .containsProduct(PRODUCT_ID_TWO, 4);
    }

    @Test
    void shouldAddProduct() {
        givenCart.empty(CART_ID);

        service.addProducts(addProductCommand(PRODUCT_ID_ONE, 13));

        thenSavedCart().hasOnlyProduct(PRODUCT_ID_ONE, 13);
    }

    @Test
    void shouldNotAddProductWhenQuantityIsLowerThanOne() {
        givenCart.empty(CART_ID);

        Executable executable = () -> service.addProducts(addProductCommand(PRODUCT_ID_ONE, -13));

        thenCartNotSavedDueToExceptionThat(executable).hasMessage("Quantity: \"-13\" is not greater than zero.");
    }

    @Test
    void shouldIncreaseQuantityOfProductWhenAlreadyInCart() {
        givenCart.withProduct(PRODUCT_ID_ONE, 3).with(CART_ID);

        service.addProducts(addProductCommand(PRODUCT_ID_ONE, 12));

        thenSavedCart().hasOnlyProduct(PRODUCT_ID_ONE, 15);
    }

    @Test
    void shouldAddProducts() {
        givenCart.empty(CART_ID);
        AddProductsCommand command = addProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 13,
                PRODUCT_ID_TWO, 26,
                PRODUCT_ID_THREE, 42));

        service.addProducts(command);

        thenSavedCart()
                .hasProducts(3)
                .containsProduct(PRODUCT_ID_ONE, 13)
                .containsProduct(PRODUCT_ID_TWO, 26)
                .containsProduct(PRODUCT_ID_THREE, 42);
    }

    @Test
    void shouldNotAddAnyProductsWhenQuantityOfAtLeastOneIsLowerThanOne() {
        givenCart.empty(CART_ID);
        AddProductsCommand command = addProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 13,
                PRODUCT_ID_TWO, -26,
                PRODUCT_ID_THREE, -42));

        Executable executable = () -> service.addProducts(command);

        thenCartNotSavedDueToExceptionThat(executable).hasMessage("Quantity: \"-26\" is not greater than zero.");
    }

    @Test
    void shouldUpdateProductsWhenSomeWereAlreadyInCartAndSomeAdded() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .withProduct(PRODUCT_ID_THREE, 7)
                .with(CART_ID);
        AddProductsCommand command = addProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 9,
                PRODUCT_ID_FOUR, 9,
                PRODUCT_ID_FIVE, 11));

        service.addProducts(command);

        thenSavedCart()
                .hasProducts(5)
                .containsProduct(PRODUCT_ID_ONE, 16)
                .containsProduct(PRODUCT_ID_TWO, 10)
                .containsProduct(PRODUCT_ID_THREE, 7)
                .containsProduct(PRODUCT_ID_FOUR, 9)
                .containsProduct(PRODUCT_ID_FIVE, 11);
    }

    @Test
    void shouldRemoveNothingWhenNothingGiven() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .with(CART_ID);

        service.addProducts(addProductCommand(emptyMap()));

        thenSavedCart()
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 14)
                .containsProduct(PRODUCT_ID_TWO, 1);
    }

    private AbstractThrowableAssert<?, RuntimeException> thenCartNotSavedDueToExceptionThat(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenCartNotSaved();

        return assertThat(actual);
    }

    private void thenCartNotSaved() {
        then(cartRepository).should(never()).save(any());
    }

    private AddProductsCommand addProductCommand(UUID productId, int quantity) {
        return addProductCommand(ImmutableMap.of(productId, quantity));
    }

    private AddProductsCommand addProductCommand(Map<UUID, Integer> products) {
        return new AddProductsCommand(CART_ID, products);
    }

    @Test
    void shouldRemoveNothingWhenProductNotInCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .with(CART_ID);
        RemoveProductsCommand command = removeProductCommand(ImmutableMap.of(
                randomId(), 22, randomId(), 12, randomId(), 7));

        service.removeProducts(command);

        thenSavedCart()
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 14)
                .containsProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldRemoveProductFromCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .withProduct(PRODUCT_ID_THREE, 7)
                .with(CART_ID);

        service.removeProducts(removeProductCommand(PRODUCT_ID_THREE, 7));

        thenSavedCart()
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 14)
                .containsProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldDecreaseProductQuantity() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .with(CART_ID);

        service.removeProducts(removeProductCommand(PRODUCT_ID_ONE, 7));

        thenSavedCart()
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 7)
                .containsProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldRemoveProductFromCartWhenQuantityGreaterThenInCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .with(CART_ID);

        service.removeProducts(removeProductCommand(PRODUCT_ID_ONE, 21));

        thenSavedCart().hasOnlyProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldRemoveProductsDecreaseQuantityOfProductsAndIgnoreThoseNotInCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .withProduct(PRODUCT_ID_THREE, 7)
                .withProduct(PRODUCT_ID_FOUR, 11)
                .with(CART_ID);
        RemoveProductsCommand command = removeProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 9,
                PRODUCT_ID_THREE, 7,
                randomId(), 11));

        service.removeProducts(command);

        thenSavedCart()
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 12)
                .containsProduct(PRODUCT_ID_FOUR, 11);
    }

    private RemoveProductsCommand removeProductCommand(UUID productId, int quantity) {
        return removeProductCommand(ImmutableMap.of(productId, quantity));
    }

    private RemoveProductsCommand removeProductCommand(Map<UUID, Integer> products) {
        return new RemoveProductsCommand(CART_ID, products);
    }

    private CartAssertion thenSavedCart() {
        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        then(cartRepository).should().save(captor.capture());

        return assertCart(captor.getValue());
    }

    @Test
    void shouldRecognizeNoProductsWereChoose() {
        givenCart
                .withProduct(randomId(), 13)
                .withProduct(randomId(), 42)
                .with(CART_ID);

        Executable executable = () -> service.chooseProducts(chooseProductsCommand(emptyMap()));

        thenOfferNotCreatedDueToCartProductsExceptionThat(executable).hasMessage("Cannot create Offer when no products were choose.");
    }

    @Test
    void shouldRecognizeProductsAreNotFromCart() {
        givenCart
                .withProduct(randomId(), 13)
                .withProduct(randomId(), 42)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 22,
                PRODUCT_ID_TWO, 13));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToCartProductsExceptionThat(executable)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 22)
                .containsProduct(PRODUCT_ID_TWO, 13);
    }

    @Test
    void shouldRecognizeChosenProductHasGreaterQuantityThanCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .withProduct(PRODUCT_ID_TWO, 7)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 22,
                PRODUCT_ID_TWO, 9));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToCartProductsExceptionThat(executable)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 22)
                .containsProduct(PRODUCT_ID_TWO, 9);
    }

    @Test
    void shouldRecognizeOneOfChosenProductHasGreaterQuantityThanCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .withProduct(PRODUCT_ID_TWO, 7)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 22,
                PRODUCT_ID_TWO, 2));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToCartProductsExceptionThat(executable)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasOnlyOneProduct(PRODUCT_ID_ONE, 22);
    }

    @Test
    void shouldRecognizeOneOfChosenProductsIsNotInTheCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .withProduct(randomId(), 7)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 10,
                PRODUCT_ID_THREE, 9));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToCartProductsExceptionThat(executable)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasOnlyOneProduct(PRODUCT_ID_THREE, 9);
    }

    private CartProductsExceptionAssertion thenOfferNotCreatedDueToCartProductsExceptionThat(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenOfferNotSaved();

        return assertCartProductsException(actual);
    }

    @Test
    void shouldRecognizeUnsupportedDeliveryMethod() {
        givenDelivery().unsupportedMethod();
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductsCommand(PRODUCT_ID_ONE, 10);

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToOfferExceptionThat(executable)
                .hasMessage("Delivery Method: " + DELIVERY_METHOD_ID + " is not supported.");
    }

    @Test
    void shouldRecognizeNotExistingDeliveryAddress() {
        givenDelivery().notExistingAddress();
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductsCommand(PRODUCT_ID_ONE, 10);

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToOfferExceptionThat(executable)
                .hasMessage("Delivery address do not exists")
                .hasDeliveryAddress(DELIVERY_ADDRESS);
    }

    @Test
    void shouldRecognizeProductsThatAreNotAvailableAnymore() {
        givenDelivery().valid(DELIVERY_PRICE);
        givenCart
                .withProduct(PRODUCT_ID_ONE, 2)
                .withProduct(PRODUCT_ID_TWO, 7)
                .withProduct(PRODUCT_ID_THREE, 4)
                .with(CART_ID);
        givenAvailability
                .notAvailable(PRODUCT_ID_ONE)
                .available(SELLER_ONE, PRODUCT_ID_TWO, 7, PRICE_ONE)
                .available(SELLER_TWO, PRODUCT_ID_THREE, 4, PRICE_TWO)
                .forChecking();
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 7,
                PRODUCT_ID_THREE, 3));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToOfferExceptionThat(executable)
                .hasMessage("Cannot create Offer because products are not available anymore.")
                .hasOnlyOneProduct(PRODUCT_ID_ONE, 2);
    }

    @Test
    void shouldRecognizeProductsWithNotEnoughQuantityAnymore() {
        givenDelivery().valid(DELIVERY_PRICE);
        givenCart
                .withProduct(PRODUCT_ID_ONE, 2)
                .withProduct(PRODUCT_ID_TWO, 7)
                .withProduct(PRODUCT_ID_THREE, 4)
                .with(CART_ID);
        givenAvailability
                .available(SELLER_ONE, PRODUCT_ID_ONE, 1, PRICE_ONE)
                .available(SELLER_ONE, PRODUCT_ID_TWO, 6, PRICE_TWO)
                .available(SELLER_TWO, PRODUCT_ID_THREE, 4, PRICE_THREE)
                .forChecking();
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 7,
                PRODUCT_ID_THREE, 3));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToOfferExceptionThat(executable)
                .hasMessage("Cannot create Offer because products are not available anymore.")
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 2)
                .containsProduct(PRODUCT_ID_TWO, 7);
    }

    private OfferExceptionAssertion thenOfferNotCreatedDueToOfferExceptionThat(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenOfferNotSaved();

        return assertOfferProductsException(actual);
    }

    @Test
    void shouldCreateOffer() {
        givenDelivery().valid(DELIVERY_PRICE);
        given(clock.nowDateTime()).willReturn(CREATED_AT);
        givenCart
                .withProduct(PRODUCT_ID_ONE, 2)
                .withProduct(PRODUCT_ID_TWO, 7)
                .withProduct(PRODUCT_ID_THREE, 4)
                .with(CART_ID);
        givenAvailability
                .available(SELLER_ONE, PRODUCT_ID_ONE, 2, PRICE_ONE)
                .available(SELLER_ONE, PRODUCT_ID_TWO, 8, PRICE_TWO)
                .available(SELLER_TWO, PRODUCT_ID_THREE, 4, PRICE_THREE)
                .forChecking();
        ChooseProductsCommand command = chooseProductsCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 7,
                PRODUCT_ID_THREE, 3));

        service.chooseProducts(command);

        thenSavedOffer()
                .hasBuyerId(BUYER_ID)
                .hasOfferNumberThatStartsWith("Offer/" + BUYER_ID + "/2023/09/25/")
                .hasCreationDateTime(CREATED_AT)
                .hasDelivery(DELIVERY_METHOD_ID, DELIVERY_ADDRESS, DELIVERY_PRICE)
                .hasProducts(3)
                .containsProduct(SELLER_ONE, PRODUCT_ID_ONE, 2, PRICE_ONE)
                .containsProduct(SELLER_ONE, PRODUCT_ID_TWO, 7, PRICE_TWO)
                .containsProduct(SELLER_TWO, PRODUCT_ID_THREE, 3, PRICE_THREE);
    }

    private GivenDelivery givenDelivery() {
        return givenDelivery.forRequest(DELIVERY_METHOD_ID, DELIVERY_ADDRESS);
    }

    private void thenOfferNotSaved() {
        then(offerRepository).should(never()).save(any());
    }

    private OfferAssertion thenSavedOffer() {
        ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
        then(offerRepository).should().save(captor.capture());

        return assertOffer(captor.getValue());
    }

    private ChooseProductsCommand chooseProductsCommand(UUID productId, int quantity) {
        return chooseProductsCommand(ImmutableMap.of(productId, quantity));
    }

    private ChooseProductsCommand chooseProductsCommand(Map<UUID, Integer> products) {
        return new ChooseProductsCommand(BUYER_ID, CART_ID, products, DELIVERY_METHOD_ID, ADDRESS_DTO);
    }

    private static AddressDto randomAddressDto() {
        Address address = FAKER.address();
        return new AddressDto(address.streetAddress(), address.city(), address.postcode(), address.country());
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }
}