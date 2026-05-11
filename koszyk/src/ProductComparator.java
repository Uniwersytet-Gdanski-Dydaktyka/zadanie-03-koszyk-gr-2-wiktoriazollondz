import java.util.Comparator;

public class ProductComparator {
    public static final Comparator<Product> DEFAULT = Comparator
            .comparingDouble(Product::price)
            .reversed()
            .thenComparing(Product::name);
}
