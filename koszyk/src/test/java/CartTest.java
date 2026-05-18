import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CartTest {
    private Cart cart;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
    }

    @Test
    public void testDefaultSortPricesDescendingAndNamesAlphabetically() {
        // dwa produkty mają tę samą cenę 20.0
        cart.addProduct(new Product("001", "Skarpetki", 20.0));
        cart.addProduct(new Product("002", "Buty", 350.0));
        cart.addProduct(new Product("003", "Czapka", 20.0));

        // domyślny komparator (cena malejąco, nazwa A-Z)
        cart.sort(ProductComparator.DEFAULT);
        List<Product> products = cart.getProducts();

        assertEquals("Buty", products.get(0).name());   // najdroższe buty pierwsze
        assertEquals("Czapka", products.get(1).name()); // taka sama cena, ale alfabetycznie C przed S
        assertEquals("Skarpetki", products.get(2).name());
    }

    @Test
    public void testFlexibleSortByAnyCriterion() {
        cart.addProduct(new Product("001", "Skarpetki", 20.0));
        cart.addProduct(new Product("002", "Buty", 350.0));
        
        // custom sort: po nazwie alfabetycznie (A-Z)
        cart.sort(Comparator.comparing(Product::name));
        List<Product> products = cart.getProducts();

        assertEquals("Buty", products.get(0).name());
        assertEquals("Skarpetki", products.get(1).name());
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
    public void testGetNCheapestAndNMostExpensive() {
        cart.addProduct(new Product("001", "Skarpetki", 20.0));
        cart.addProduct(new Product("002", "Buty", 350.0));
        cart.addProduct(new Product("003", "Koszulka", 80.0));

        // 2 najtańsze
        List<Product> cheapestTwo = cart.getNCheapest(2);
        assertEquals(2, cheapestTwo.size());
        assertEquals("Skarpetki", cheapestTwo.get(0).name());
        assertEquals("Koszulka", cheapestTwo.get(1).name());

        // 2 najdroższe
        List<Product> mostExpensiveTwo = cart.getNMostExpensive(2);
        assertEquals(2, mostExpensiveTwo.size());
        assertEquals("Buty", mostExpensiveTwo.get(0).name());
        assertEquals("Koszulka", mostExpensiveTwo.get(1).name());
    }

    @Test
    public void testValueThresholdDiscountAbove300() {
        cart.addProduct(new Product("001", "Buty", 400.0));
        cart.applyDiscount(new Discounts.ValueThresholdDiscount());
        
        // 400 * 0.95 = 380.0
        assertEquals(380.0, cart.getTotal());
    }

    @Test
    public void testBuy2Get1FreeDiscount() {
        cart.addProduct(new Product("001", "Buty", 300.0));
        cart.addProduct(new Product("002", "Koszulka", 100.0));
        cart.addProduct(new Product("003", "Skarpetki", 50.0));

        cart.applyDiscount(new Discounts.Buy2Get1FreeDiscount());

        // skarpetki za 0zł, suma = 300 + 100 + 0 = 400
        assertEquals(400.0, cart.getTotal());
    }

    @Test
    public void testGetDiscountName() {
        DiscountStrategy valueDiscount = new Discounts.ValueThresholdDiscount();
        DiscountStrategy buy2get1 = new Discounts.Buy2Get1FreeDiscount();

        assertEquals("Rabat 5% powyżej 300zł", valueDiscount.getName());
        assertEquals("Promocja 2+1 Gratis (najtańszy darmowy)", buy2get1.getName());
    }

    @Test
    public void testDiscountOptimizerSelectsBestOption() {
        cart.addProduct(new Product("001", "Buty", 350.0));
        cart.addProduct(new Product("002", "Skarpetki", 20.0));
        cart.addProduct(new Product("003", "Koszulka", 80.0));

        List<DiscountStrategy> strategies = Arrays.asList(
            new Discounts.ValueThresholdDiscount(), 
            new Discounts.Buy2Get1FreeDiscount()    
        );

        List<Product> bestOption = Discounts.DiscountOptimizer.getBestOption(cart.getProducts(), strategies);
        double bestTotal = bestOption.stream().mapToDouble(Product::discountPrice).sum();

        // 450 * 0,95 = 427,5     <     350 + 80 + 0 = 430
        assertEquals(427.5, bestTotal);
        assertEquals("Rabat 5% powyżej 300zł", Discounts.DiscountOptimizer.bestStrategy.getName());
    }

    @Test
    public void testEdgeCaseEmptyCart() {
        assertEquals(0.0, cart.getTotal());
        assertNull(cart.getCheapest());
        assertNull(cart.getMostExpensive());
        assertTrue(cart.getNCheapest(5).isEmpty());
        
        assertDoesNotThrow(() -> cart.applyDiscount(new Discounts.ValueThresholdDiscount()));
    }
}
