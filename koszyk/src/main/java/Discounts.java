import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

interface DiscountStrategy {
    List<Product> apply(List<Product> products);
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
    }

    // logika do zadania dodatkowego
    public static class DiscountOptimizer {
        public static List<Product> getBestOption(List<Product> baseProducts, List<DiscountStrategy> availableStrategies) {
            List<Product> bestProducts = baseProducts;
            double lowestTotal = Double.MAX_VALUE;

            for (DiscountStrategy strategy : availableStrategies) {
                // wykonujemy promocję na kopii listy
                List<Product> currentResult = strategy.apply(new ArrayList<>(baseProducts));
                
                // liczymy sumę dla tej konkretnej promocji
                double currentTotal = currentResult.stream()
                        .mapToDouble(Product::discountPrice)
                        .sum();

                // jeśli ta promocja daje niższą cenę niż poprzednie -> zapamiętujemy ją
                if (currentTotal < lowestTotal) {
                    lowestTotal = currentTotal;
                    bestProducts = currentResult;
                }
            }
            return bestProducts;
        }
    }
}
