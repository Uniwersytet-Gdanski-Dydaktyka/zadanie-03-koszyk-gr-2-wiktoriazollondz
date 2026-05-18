import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) { products.add(p); }

    public void applyDiscount(DiscountStrategy strategy) {
        this.products = strategy.apply(this.products);
    }

    public void sort(java.util.Comparator<Product> comparator) {
        this.products.sort(comparator);
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
    
    public Product getCheapest() {
        if (products.isEmpty()) return null;
        return products.stream()
                .min(java.util.Comparator.comparingDouble(Product::price))
                .orElse(null);
    }

    public Product getMostExpensive() {
        if (products.isEmpty()) return null;
        return products.stream()
                .max(java.util.Comparator.comparingDouble(Product::price))
                .orElse(null);
    }

    public List<Product> getNCheapest(int n) {
        return products.stream()
                .sorted(java.util.Comparator.comparingDouble(Product::price))
                .limit(n)
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Product> getNMostExpensive(int n) {
        return products.stream()
                .sorted(java.util.Comparator.comparingDouble(Product::price).reversed())
                .limit(n)
                .collect(java.util.stream.Collectors.toList());
    }
}
