import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.addProduct(new Product("001", "Buty", 350.0));
        cart.addProduct(new Product("002", "Skarpetki", 20.0));
        cart.addProduct(new Product("003", "Koszulka", 80.0));

        System.out.println("--- Produkty w koszyku ---");
        cart.printCart();

        // lista dostępnych promocji
        List<DiscountStrategy> strategies = Arrays.asList(
            new Discounts.ValueThresholdDiscount(),
            new Discounts.Buy2Get1FreeDiscount(),
            new Discounts.FreeGiftDiscount()
        );

        // uruchamiamy optymalizator (zadanie dodatkowe)
        System.out.println("\n--- Szukam najlepszej promocji dla klienta... ---");
        List<Product> bestResult = Discounts.DiscountOptimizer.getBestOption(cart.getProducts(), strategies);
        
        // wyświetlamy wynik
        double total = bestResult.stream().mapToDouble(Product::discountPrice).sum();
        System.out.println("Najlepsza opcja daje sumę: " + total + " zł");
        bestResult.forEach(p -> System.out.println("- " + p.name() + ": " + p.discountPrice()));
    }
}