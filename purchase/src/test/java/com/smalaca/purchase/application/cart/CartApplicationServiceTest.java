package com.smalaca.purchase.application.cart;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.smalaca.purchase.domain.cart.Cart;
import com.smalaca.purchase.domain.cart.CartId;
import com.smalaca.purchase.domain.cart.CartRepository;
import com.smalaca.purchase.domain.offer.Clock;
import com.smalaca.purchase.domain.offer.Offer;
import com.smalaca.purchase.domain.offer.OfferRepository;
import com.smalaca.purchase.domain.offer.ProductManagementService;
import com.smalaca.purchase.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;
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
    private static final UUID CART_UUID = randomId();
    private static final CartId CART_ID = new CartId(CART_UUID);
    private static final LocalDateTime CREATED_AT = LocalDateTime.now();

    private final OfferRepository offerRepository = mock(OfferRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ProductManagementService productManagementService = mock(ProductManagementService.class);
    private final Clock clock = mock(Clock.class);
    private final CartApplicationService service = CartApplicationService.create(cartRepository, offerRepository, productManagementService, clock);

    @Test
    void shouldAddNothingWhenNothingGiven() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 12),
                Product.product(productIdTwo, 4)));

        service.addProduct(addProductCommand(emptyMap()));

        assertCart(thenCartWasSaved())
                .hasProducts(2)
                .hasProduct(productIdOne, 12)
                .hasProduct(productIdTwo, 4);
    }

    @Test
    void shouldAddProduct() {
        givenEmptyCart();
        UUID productId = randomId();

        service.addProduct(addProductCommand(ImmutableMap.of(productId, 13)));

        assertCart(thenCartWasSaved()).hasOnlyProduct(productId, 13);
    }

    @Test
    void shouldNotAddProductWhenAmountIsLowerThanOne() {
        givenEmptyCart();

        Executable executable = () -> service.addProduct(addProductCommand(ImmutableMap.of(randomId(), -13)));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Amount: \"-13\" is not greater than zero.");
        thenCartWasNotSaved();
    }

    @Test
    void shouldIncreaseAmountOfProductWhenAlreadyInCart() {
        UUID productId = randomId();
        givenCartWith(ImmutableList.of(Product.product(productId, 3)));

        service.addProduct(addProductCommand(ImmutableMap.of(productId, 12)));

        assertCart(thenCartWasSaved()).hasOnlyProduct(productId, 15);
    }

    @Test
    void shouldAddProducts() {
        givenEmptyCart();
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        AddProductCommand command = addProductCommand(ImmutableMap.of(
                productIdOne, 13,
                productIdTwo, 26,
                productIdThree, 42));

        service.addProduct(command);

        assertCart(thenCartWasSaved())
                .hasProducts(3)
                .hasProduct(productIdOne, 13)
                .hasProduct(productIdTwo, 26)
                .hasProduct(productIdThree, 42);
    }

    @Test
    void shouldNotAddAnyProductsWhenAmountIsLowerThanOne() {
        givenEmptyCart();
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        AddProductCommand command = addProductCommand(ImmutableMap.of(
                productIdOne, 13,
                productIdTwo, -26,
                productIdThree, -42));

        Executable executable = () -> service.addProduct(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertThat(actual).hasMessage("Amount: \"-26\" is not greater than zero.");
        thenCartWasNotSaved();
    }

    @Test
    void shouldUpdateProductsWhenSomeWereAlreadyInCartAndSomeAdded() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        UUID productIdFour = randomId();
        UUID productIdFive = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1),
                Product.product(productIdThree, 7)));
        AddProductCommand command = addProductCommand(ImmutableMap.of(
                productIdOne, 2,
                productIdTwo, 9,
                productIdFour, 9,
                productIdFive, 11));

        service.addProduct(command);

        assertCart(thenCartWasSaved())
                .hasProducts(5)
                .hasProduct(productIdOne, 16)
                .hasProduct(productIdTwo, 10)
                .hasProduct(productIdThree, 7)
                .hasProduct(productIdFour, 9)
                .hasProduct(productIdFive, 11);
    }

    @Test
    void shouldRemoveNothingWhenNothingGiven() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1)));

        service.addProduct(addProductCommand(emptyMap()));

        assertCart(thenCartWasSaved())
                .hasProducts(2)
                .hasProduct(productIdOne, 14)
                .hasProduct(productIdTwo, 1);
    }

    private AddProductCommand addProductCommand(Map<UUID, Integer> products) {
        return new AddProductCommand(CART_UUID, products);
    }

    @Test
    void shouldRemoveNothingWhenProductNotInCart() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1)));
        RemoveProductCommand command = removeProductCommand(ImmutableMap.of(
                randomId(), 22, randomId(), 12, randomId(), 7));

        service.removeProduct(command);

        assertCart(thenCartWasSaved())
                .hasProducts(2)
                .hasProduct(productIdOne, 14)
                .hasProduct(productIdTwo, 1);
    }

    @Test
    void shouldRemoveProductFromCart() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1),
                Product.product(productIdThree, 7)));
        RemoveProductCommand command = removeProductCommand(ImmutableMap.of(productIdThree, 7));

        service.removeProduct(command);

        assertCart(thenCartWasSaved())
                .hasProducts(2)
                .hasProduct(productIdOne, 14)
                .hasProduct(productIdTwo, 1);
    }

    @Test
    void shouldDecreaseProductAmount() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1)));
        RemoveProductCommand command = removeProductCommand(ImmutableMap.of(productIdOne, 7));

        service.removeProduct(command);

        assertCart(thenCartWasSaved())
                .hasProducts(2)
                .hasProduct(productIdOne, 7)
                .hasProduct(productIdTwo, 1);
    }

    @Test
    void shouldRemoveProductFromCartWhenAmountGreaterThenInCart() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1)));
        RemoveProductCommand command = removeProductCommand(ImmutableMap.of(productIdOne, 21));

        service.removeProduct(command);

        assertCart(thenCartWasSaved()).hasOnlyProduct(productIdTwo, 1);
    }

    @Test
    void shouldRemoveProductsDecreaseAmountOfProductsAndIgnoreThoseNotInCart() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        UUID productIdFour = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 14),
                Product.product(productIdTwo, 1),
                Product.product(productIdThree, 7),
                Product.product(productIdFour, 11)));
        RemoveProductCommand command = removeProductCommand(ImmutableMap.of(
                productIdOne, 2,
                productIdTwo, 9,
                productIdThree, 7,
                randomId(), 11));

        service.removeProduct(command);

        assertCart(thenCartWasSaved())
                .hasProducts(2)
                .hasProduct(productIdOne, 12)
                .hasProduct(productIdFour, 11);
    }

    private RemoveProductCommand removeProductCommand(Map<UUID, Integer> products) {
        return new RemoveProductCommand(CART_UUID, products);
    }

    @Test
    void shouldRecognizeNoProductsWereChoose() {
        givenCartWith(ImmutableList.of(
                Product.product(randomId(), 13),
                Product.product(randomId(), 42)));

        Executable executable = () -> service.chooseProducts(chooseProductCommand(emptyMap()));

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertCartProductsException(actual).hasMessage("Cannot create Offer when no products were choose.");
    }

    @Test
    void shouldRecognizeProductsAreNotFromCart() {
        givenCartWith(ImmutableList.of(
                Product.product(randomId(), 13),
                Product.product(randomId(), 42)));
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 22,
                productIdTwo, 13));

        Executable executable = () -> service.chooseProducts(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertCartProductsException(actual)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasProducts(2)
                .containsProduct(productIdOne, 22)
                .containsProduct(productIdTwo, 13);
    }

    @Test
    void shouldRecognizeChosenProductHasGreaterAmountThanCart() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 13),
                Product.product(productIdTwo, 7)));
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 22,
                productIdTwo, 9));

        Executable executable = () -> service.chooseProducts(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertCartProductsException(actual)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasProducts(2)
                .containsProduct(productIdOne, 22)
                .containsProduct(productIdTwo, 9);
    }

    @Test
    void shouldRecognizeOneOfChosenProductHasGreaterAmountThanCart() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 13),
                Product.product(productIdTwo, 7)));
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 22,
                productIdTwo, 2));

        Executable executable = () -> service.chooseProducts(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertCartProductsException(actual)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasOnlyOneProduct(productIdOne, 22);
    }

    @Test
    void shouldRecognizeOneOfChosenProductsIsNotInTheCart() {
        UUID productIdOne = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 13),
                Product.product(randomId(), 7)));
        UUID productIdThree = randomId();
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 10,
                productIdThree, 9));

        Executable executable = () -> service.chooseProducts(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertCartProductsException(actual)
                .hasMessage("Cannot create Offer when products are not in the Cart.")
                .hasOnlyOneProduct(productIdThree, 9);
    }

    @Test
    void shouldRecognizeProductsThatAreNotAvailableAnymore() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 2),
                Product.product(productIdTwo, 7),
                Product.product(productIdThree, 4)));
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 2,
                productIdTwo, 7,
                productIdThree, 3));
        given(productManagementService.getAvailabilityOf(ImmutableList.of(productIdOne, productIdTwo, productIdThree))).willReturn(ImmutableList.of(
                Product.product(productIdTwo, 7),
                Product.product(productIdThree, 4)));

        Executable executable = () -> service.chooseProducts(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertOfferProductsException(actual)
                .hasMessage("Cannot create Offer because products are not available anymore.")
                .hasOnlyOneProduct(productIdOne, 2);
    }

    @Test
    void shouldRecognizeProductsWithNotEnoughAmountAnymore() {
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 2),
                Product.product(productIdTwo, 7),
                Product.product(productIdThree, 4)));
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 2,
                productIdTwo, 7,
                productIdThree, 3));
        given(productManagementService.getAvailabilityOf(ImmutableList.of(productIdOne, productIdTwo, productIdThree))).willReturn(ImmutableList.of(
                Product.product(productIdOne, 1),
                Product.product(productIdTwo, 6),
                Product.product(productIdThree, 4)));

        Executable executable = () -> service.chooseProducts(command);

        RuntimeException actual = assertThrows(RuntimeException.class, executable);
        assertOfferProductsException(actual)
                .hasMessage("Cannot create Offer because products are not available anymore.")
                .hasProducts(2)
                .containsProduct(productIdOne, 2)
                .containsProduct(productIdTwo, 7);
    }

    @Test
    void shouldCreateOffer() {
        given(clock.nowDateTime()).willReturn(CREATED_AT);
        UUID productIdOne = randomId();
        UUID productIdTwo = randomId();
        UUID productIdThree = randomId();
        givenCartWith(ImmutableList.of(
                Product.product(productIdOne, 2),
                Product.product(productIdTwo, 7),
                Product.product(productIdThree, 4)));
        ChooseProductCommand command = chooseProductCommand(ImmutableMap.of(
                productIdOne, 2,
                productIdTwo, 7,
                productIdThree, 3));
        given(productManagementService.getAvailabilityOf(ImmutableList.of(productIdOne, productIdTwo, productIdThree))).willReturn(ImmutableList.of(
                Product.product(productIdOne, 2),
                Product.product(productIdTwo, 8),
                Product.product(productIdThree, 4)));

        service.chooseProducts(command);

        assertOffer(thenOfferWasSaved())
                .hasCreationDateTime(CREATED_AT);
                // offer number
                // products with the price -> refactoring first
                // delivery methods with price -> refactoring first
    }

    private ChooseProductCommand chooseProductCommand(Map<UUID, Integer> products) {
        return new ChooseProductCommand(CART_UUID, products);
    }

    private void givenEmptyCart() {
        given(cartRepository.findBy(CART_ID)).willReturn(new Cart());
    }

    private void givenCartWith(List<Product> products) {
        Cart cart = new Cart();
        cart.add(products);
        given(cartRepository.findBy(CART_ID)).willReturn(cart);
    }

    private Cart thenCartWasSaved() {
        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);
        then(cartRepository).should().save(captor.capture());
        return captor.getValue();
    }

    private Offer thenOfferWasSaved() {
        ArgumentCaptor<Offer> captor = ArgumentCaptor.forClass(Offer.class);
        then(offerRepository).should().save(captor.capture());
        return captor.getValue();
    }

    private void thenCartWasNotSaved() {
        then(cartRepository).should(never()).save(any());
    }

    private static UUID randomId() {
        return UUID.randomUUID();
    }
}