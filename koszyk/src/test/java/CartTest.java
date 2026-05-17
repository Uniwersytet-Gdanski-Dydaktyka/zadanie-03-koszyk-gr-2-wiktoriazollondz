import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class CartTest {
    private Cart cart;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
    }

    @Test
    public void testSortPricesDescendingAndNamesAlphabetically() {
        cart.addProduct(new Product("001", "Skarpetki", 20.0));
        cart.addProduct(new Product("002", "Buty", 350.0));
        cart.addProduct(new Product("003", "Czapka", 20.0));

        cart.sort();
        List<Product> products = cart.getProducts();

        assertEquals("Buty", products.get(0).name());
        assertEquals("Czapka", products.get(1).name());
        assertEquals("Skarpetki", products.get(2).name());
    }

    @Test
    public void testGetCheapestAndMostExpensive() {
        cart.addProduct(new Product("001", "Skarpetki", 20.0));
        cart.addProduct(new Product("002", "Buty", 350.0));
        cart.addProduct(new Product("003", "Koszulka", 80.0));

        assertEquals("Skarpetki", cart.getCheapest().name());
        assertEquals("Buty", cart.getMostExpensive().name());
    }

    @Test
    public void testValueThresholdDiscountAbove300() {
        cart.addProduct(new Product("001", "Buty", 400.0)); // suma > 300
        cart.applyDiscount(new Discounts.ValueThresholdDiscount());
        
        // 400 * 0.95 = 380.0
        assertEquals(380.0, cart.getTotal(), 0.001);
    }

    @Test
    public void testBuy2Get1FreeDiscount() {
        cart.addProduct(new Product("001", "Buty", 300.0));
        cart.addProduct(new Product("002", "Koszulka", 100.0));
        cart.addProduct(new Product("003", "Skarpetki", 50.0)); // najtańszy

        cart.applyDiscount(new Discounts.Buy2Get1FreeDiscount());

        // skarpetki = 0 zł, więc suma = 300 + 100 + 0 = 400
        assertEquals(400.0, cart.getTotal(), 0.001);
    }

    @Test
    public void testDiscountOptimizerSelectsBestOption() {
        cart.addProduct(new Product("001", "Buty", 350.0));
        cart.addProduct(new Product("002", "Skarpetki", 20.0));
        cart.addProduct(new Product("003", "Koszulka", 80.0));

        List<DiscountStrategy> strategies = Arrays.asList(
            new Discounts.ValueThresholdDiscount(), // daje sumę 427.5 (Lepsza)
            new Discounts.Buy2Get1FreeDiscount()    // daje sumę 430.0
        );

        List<Product> bestOption = Discounts.DiscountOptimizer.getBestOption(cart.getProducts(), strategies);
        double bestTotal = bestOption.stream().mapToDouble(Product::discountPrice).sum();

        assertEquals(427.5, bestTotal, 0.001);
    }

    @Test
    public void testEdgeCaseEmptyCart() {
        // sprawdzamy sytuacje brzegowe dla pustego koszyka
        assertEquals(0.0, cart.getTotal());
        assertNull(cart.getCheapest());
        assertNull(cart.getMostExpensive());
        
        // próba nałożenia promocji na pusty koszyk nie powinna rzucić błędem
        assertDoesNotThrow(() -> cart.applyDiscount(new Discounts.ValueThresholdDiscount()));
    }
}
