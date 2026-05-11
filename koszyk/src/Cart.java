import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) { products.add(p); }

    public void applyDiscount(DiscountStrategy strategy) {
        this.products = strategy.apply(this.products);
    }

    public void sort() {
        this.products.sort(ProductComparator.DEFAULT);
    }

    public double getTotal() {
        return products.stream().mapToDouble(Product::discountPrice).sum();
    }

    public void printCart() {
        products.forEach(p -> System.out.println(p.name() + " - Cena: " + p.price() + " (Po rabacie: " + p.discountPrice() + ")"));
        System.out.println("SUMA: " + getTotal() + " zł");
    }

    public List<Product> getProducts() {
        return new ArrayList<>(this.products);
    }
    
    // Dodaj to do Cart.java
    public Product getCheapest() {
        return products.stream()
                .min(ProductComparator.DEFAULT) // Używamy Twojego komparatora!
                .orElse(null);
    }

    public Product getMostExpensive() {
        return products.stream()
                .max(ProductComparator.DEFAULT)
                .orElse(null);
    }
}
