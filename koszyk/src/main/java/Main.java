import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.addProduct(new Product("002", "Skarpetki", 20.0));
        cart.addProduct(new Product("001", "Buty", 350.0));
        cart.addProduct(new Product("003", "Koszulka", 80.0));

        System.out.println("~~~ Przed sortowaniem:");
        cart.printCart();
        
        System.out.println("\n~~~ Sortowanie (cena malejąco, potem alfabetycznie):");
        cart.sort(ProductComparator.DEFAULT);
        cart.printCart();

        // lista dostępnych promocji do sprawdzenia
        List<DiscountStrategy> strategies = Arrays.asList(
            new Discounts.ValueThresholdDiscount(),
            new Discounts.Buy2Get1FreeDiscount(),
            new Discounts.FreeGiftDiscount()
        );

        System.out.println("\n~~~ Optymalizator (jaka najlepsza promocja):");
        List<Product> bestResult = Discounts.DiscountOptimizer.getBestOption(cart.getProducts(), strategies);
        
        System.out.println("Najlepsza promocja: " + Discounts.DiscountOptimizer.bestStrategy.getName());
        
        double totalAfterDiscount = bestResult.stream().mapToDouble(Product::discountPrice).sum();
        System.out.println("Suma końcowa koszyka: " + totalAfterDiscount + " zł");
        System.out.println("Zawartość koszyka po rabatach:");
        bestResult.forEach(p -> System.out.println("- " + p.name() + " (Cena bazowa: " + p.price() + " zł -> Po rabacie: " + p.discountPrice() + " zł)"));
    }
}