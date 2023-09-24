package com.smalaca.purchase.application.cart;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartAssertion;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartProductsExceptionAssertion;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.offer.Clock;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferAssertion;
import com.smalaca.purchase.domain.offer.OfferProductsExceptionAssertion;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.offer.ProductManagementService;
import com.smalaca.purchase.domain.product.Product;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.smalaca.purchase.domain.cart.CartAssertion.assertCart;
import static com.smalaca.purchase.domain.cart.CartProductsExceptionAssertion.assertCartProductsException;
import static com.smalaca.purchase.domain.offer.OfferAssertion.assertOffer;
import static com.smalaca.purchase.domain.offer.OfferProductsExceptionAssertion.assertOfferProductsException;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

class CartApplicationServiceTest {
    private static final UUID PRODUCT_ID_ONE = randomId();
    private static final UUID PRODUCT_ID_TWO = randomId();
    private static final UUID PRODUCT_ID_THREE = randomId();
    private static final UUID PRODUCT_ID_FOUR = randomId();
    private static final UUID PRODUCT_ID_FIVE = randomId();
    private static final UUID CART_UUID = randomId();
    private static final CartId CART_ID = new CartId(CART_UUID);
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ProductManagementService productManagementService = mock(ProductManagementService.class);
    private final Clock clock = mock(Clock.class);
    private final CartApplicationService service = CartApplicationService.create(cartRepository, offerRepository, productManagementService, clock);

    private final GivenCartFactory givenCart = new GivenCartFactory(cartRepository);

    @Test
    void shouldAddNothingWhenNothingGiven() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 12)
                .withProduct(PRODUCT_ID_TWO, 4)
                .with(CART_ID);

        service.addProducts(addProductCommand(emptyMap()));

        thenSavedCart()
                .hasProducts(2)
                .hasProduct(PRODUCT_ID_ONE, 12)
                .hasProduct(PRODUCT_ID_TWO, 4);
    }

    @Test
    void shouldAddProduct() {
        givenCart.empty(CART_ID);

        service.addProducts(addProductCommand(PRODUCT_ID_ONE, 13));

        thenSavedCart().hasOnlyProduct(PRODUCT_ID_ONE, 13);
    }

    @Test
    void shouldNotAddProductWhenAmountIsLowerThanOne() {
        givenCart.empty(CART_ID);

        Executable executable = () -> service.addProducts(addProductCommand(PRODUCT_ID_ONE, -13));

        thenCartNotSavedDueToExceptionThat(executable).hasMessage("Amount: \"-13\" is not greater than zero.");
    }

    @Test
    void shouldIncreaseAmountOfProductWhenAlreadyInCart() {
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
                .hasProduct(PRODUCT_ID_ONE, 13)
                .hasProduct(PRODUCT_ID_TWO, 26)
                .hasProduct(PRODUCT_ID_THREE, 42);
    }

    @Test
    void shouldNotAddAnyProductsWhenAmountOfAtLeastOneIsLowerThanOne() {
        givenCart.empty(CART_ID);
        AddProductsCommand command = addProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 13,
                PRODUCT_ID_TWO, -26,
                PRODUCT_ID_THREE, -42));

        Executable executable = () -> service.addProducts(command);

        thenCartNotSavedDueToExceptionThat(executable).hasMessage("Amount: \"-26\" is not greater than zero.");
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
                .hasProduct(PRODUCT_ID_ONE, 16)
                .hasProduct(PRODUCT_ID_TWO, 10)
                .hasProduct(PRODUCT_ID_THREE, 7)
                .hasProduct(PRODUCT_ID_FOUR, 9)
                .hasProduct(PRODUCT_ID_FIVE, 11);
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
                .hasProduct(PRODUCT_ID_ONE, 14)
                .hasProduct(PRODUCT_ID_TWO, 1);
    }

    private AbstractThrowableAssert<?, RuntimeException> thenCartNotSavedDueToExceptionThat(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenCartNotSaved();

        return assertThat(actual);
    }

    private void thenCartNotSaved() {
        then(cartRepository).should(never()).save(any());
    }

    private AddProductsCommand addProductCommand(UUID productId, int amount) {
        return addProductCommand(ImmutableMap.of(productId, amount));
    }

    private AddProductsCommand addProductCommand(Map<UUID, Integer> products) {
        return new AddProductsCommand(CART_UUID, products);
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
                .hasProduct(PRODUCT_ID_ONE, 14)
                .hasProduct(PRODUCT_ID_TWO, 1);
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
                .hasProduct(PRODUCT_ID_ONE, 14)
                .hasProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldDecreaseProductAmount() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .with(CART_ID);

        service.removeProducts(removeProductCommand(PRODUCT_ID_ONE, 7));

        thenSavedCart()
                .hasProducts(2)
                .hasProduct(PRODUCT_ID_ONE, 7)
                .hasProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldRemoveProductFromCartWhenAmountGreaterThenInCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 14)
                .withProduct(PRODUCT_ID_TWO, 1)
                .with(CART_ID);

        service.removeProducts(removeProductCommand(PRODUCT_ID_ONE, 21));

        thenSavedCart().hasOnlyProduct(PRODUCT_ID_TWO, 1);
    }

    @Test
    void shouldRemoveProductsDecreaseAmountOfProductsAndIgnoreThoseNotInCart() {
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
                .hasProduct(PRODUCT_ID_ONE, 12)
                .hasProduct(PRODUCT_ID_FOUR, 11);
    }

    private RemoveProductsCommand removeProductCommand(UUID productId, int amount) {
        return removeProductCommand(ImmutableMap.of(productId, amount));
    }

    private RemoveProductsCommand removeProductCommand(Map<UUID, Integer> products) {
        return new RemoveProductsCommand(CART_UUID, products);
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

        Executable executable = () -> service.chooseProducts(chooseProductCommand(emptyMap()));

        thenOfferNotCreatedDueToCartProductsExceptionThat(executable).hasMessage("Cannot create Offer when no products were choose.");
    }

    @Test
    void shouldRecognizeProductsAreNotFromCart() {
        givenCart
                .withProduct(randomId(), 13)
                .withProduct(randomId(), 42)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
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
    void shouldRecognizeChosenProductHasGreaterAmountThanCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .withProduct(PRODUCT_ID_TWO, 7)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
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
    void shouldRecognizeOneOfChosenProductHasGreaterAmountThanCart() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 13)
                .withProduct(PRODUCT_ID_TWO, 7)
                .with(CART_ID);
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
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
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
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
    void shouldRecognizeProductsThatAreNotAvailableAnymore() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 2)
                .withProduct(PRODUCT_ID_TWO, 7)
                .withProduct(PRODUCT_ID_THREE, 4)
                .with(CART_ID);
        given(productManagementService.getAvailabilityOf(ImmutableList.of(PRODUCT_ID_ONE, PRODUCT_ID_TWO, PRODUCT_ID_THREE))).willReturn(ImmutableList.of(
                Product.product(PRODUCT_ID_TWO, 7),
                Product.product(PRODUCT_ID_THREE, 4)));
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 7,
                PRODUCT_ID_THREE, 3));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToOfferProductsExceptionThat(executable)
                .hasMessage("Cannot create Offer because products are not available anymore.")
                .hasOnlyOneProduct(PRODUCT_ID_ONE, 2);
    }

    @Test
    void shouldRecognizeProductsWithNotEnoughAmountAnymore() {
        givenCart
                .withProduct(PRODUCT_ID_ONE, 2)
                .withProduct(PRODUCT_ID_TWO, 7)
                .withProduct(PRODUCT_ID_THREE, 4)
                .with(CART_ID);
        given(productManagementService.getAvailabilityOf(ImmutableList.of(PRODUCT_ID_ONE, PRODUCT_ID_TWO, PRODUCT_ID_THREE))).willReturn(ImmutableList.of(
                Product.product(PRODUCT_ID_ONE, 1),
                Product.product(PRODUCT_ID_TWO, 6),
                Product.product(PRODUCT_ID_THREE, 4)));
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 7,
                PRODUCT_ID_THREE, 3));

        Executable executable = () -> service.chooseProducts(command);

        thenOfferNotCreatedDueToOfferProductsExceptionThat(executable)
                .hasMessage("Cannot create Offer because products are not available anymore.")
                .hasProducts(2)
                .containsProduct(PRODUCT_ID_ONE, 2)
                .containsProduct(PRODUCT_ID_TWO, 7);
    }

    private OfferProductsExceptionAssertion thenOfferNotCreatedDueToOfferProductsExceptionThat(Executable executable) {
        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        thenOfferNotSaved();

        return assertOfferProductsException(actual);
    }

    @Test
    void shouldCreateOffer() {
        given(clock.nowDateTime()).willReturn(CREATED_AT);
        givenCart
                .withProduct(PRODUCT_ID_ONE, 2)
                .withProduct(PRODUCT_ID_TWO, 7)
                .withProduct(PRODUCT_ID_THREE, 4)
                .with(CART_ID);
        given(productManagementService.getAvailabilityOf(ImmutableList.of(PRODUCT_ID_ONE, PRODUCT_ID_TWO, PRODUCT_ID_THREE))).willReturn(ImmutableList.of(
                Product.product(PRODUCT_ID_ONE, 2),
                Product.product(PRODUCT_ID_TWO, 8),
                Product.product(PRODUCT_ID_THREE, 4)));
        ChooseProductsCommand command = chooseProductCommand(ImmutableMap.of(
                PRODUCT_ID_ONE, 2,
                PRODUCT_ID_TWO, 7,
                PRODUCT_ID_THREE, 3));

        service.chooseProducts(command);

        thenSavedOffer()
                .hasCreationDateTime(CREATED_AT);
                // offer number
                // products with the price -> refactoring first
                // delivery methods with price -> refactoring first
    }

    private void thenOfferNotSaved() {
        then(offerRepository).should(never()).save(any());
    }

    private OfferAssertion thenSavedOffer() {
        ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
        then(offerRepository).should().save(captor.capture());

        return assertOffer(captor.getValue());
    }

    private ChooseProductsCommand chooseProductCommand(Map<UUID, Integer> products) {
        return new ChooseProductsCommand(CART_UUID, products);
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }
}