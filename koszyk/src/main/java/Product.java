public record Product(String code, String name, double price, double discountPrice) {
    public Product(String code, String name, double price) {
        this(code, name, price, price);
    }

    public Product withDiscountPrice(double newPrice) {
        return new Product(this.code, this.name, this.price, newPrice);
    }
}