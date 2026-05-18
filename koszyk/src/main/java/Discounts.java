import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface DiscountStrategy {
    List<Product> apply(List<Product> products);
    String getName();
}

public class Discounts {

    // powyżej 300 zł -> 5% zniżki
    public static class ValueThresholdDiscount implements DiscountStrategy {
        public List<Product> apply(List<Product> products) {
            double total = products.stream().mapToDouble(Product::price).sum();
            if (total > 300) {
                return products.stream()
                        .map(p -> p.withDiscountPrice(p.price() * 0.95))
                        .collect(Collectors.toList());
            }
            return products;
        }
        @Override public String getName() { return "Rabat 5% powyżej 300zł"; }
    }

    // 2+1 gratis
    public static class Buy2Get1FreeDiscount implements DiscountStrategy {
        public List<Product> apply(List<Product> products) {
            if (products.size() < 3) return products;
            List<Product> sorted = new ArrayList<>(products);
            sorted.sort(ProductComparator.DEFAULT); // najtańszy wyląduje na końcu przez reversed()
            int lastIndex = sorted.size() - 1;
            sorted.set(lastIndex, sorted.get(lastIndex).withDiscountPrice(0.0));
            return sorted;
        }
        @Override public String getName() { return "Promocja 2+1 Gratis (najtańszy darmowy)"; }
    }

    // darmowy kubek powyżej 200 zł
    public static class FreeGiftDiscount implements DiscountStrategy {
        public List<Product> apply(List<Product> products) {
            double total = products.stream().mapToDouble(Product::price).sum();
            if (total > 200) {
                List<Product> copy = new ArrayList<>(products);
                copy.add(new Product("GIFT", "Firmowy Kubek", 0.0, 0.0));
                return copy;
            }
            return products;
        }
        @Override public String getName() { return "Darmowy kubek firmowy powyżej 200zł"; }
    }

    // logika do zadania dodatkowego
    public static class DiscountOptimizer {
        public static DiscountStrategy bestStrategy;

        public static List<Product> getBestOption(List<Product> baseProducts, List<DiscountStrategy> availableStrategies) {
            List<Product> bestProducts = baseProducts;
            double lowestTotal = Double.MAX_VALUE;
            bestStrategy = null;

            for (DiscountStrategy strategy : availableStrategies) {
                List<Product> currentResult = strategy.apply(new ArrayList<>(baseProducts));
                double currentTotal = currentResult.stream().mapToDouble(Product::discountPrice).sum();

                if (currentTotal < lowestTotal) {
                    lowestTotal = currentTotal;
                    bestProducts = currentResult;
                    bestStrategy = strategy;
                }
            }
            return bestProducts;
        }
    }
}
