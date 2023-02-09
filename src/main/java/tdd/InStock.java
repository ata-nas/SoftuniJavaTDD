package tdd;

import java.util.*;
import java.util.stream.Collectors;

public class InStock implements ProductStock {

    private List<Product> stock;

    public InStock() {

        this.stock = new ArrayList<>();

    }


    @Override
    public int getCount() {

        return stock.size();

    }

    @Override
    public boolean contains(Product product) {

        return stock.stream().anyMatch(p -> p.getLabel().equals(product.getLabel()));

    }

    @Override
    public void add(Product product) {

        stock.add(product);

    }

    @Override
    public void changeQuantity(String label, int quantity) {

        Product target = findByLabel(label);
        target.setQuantity(quantity);

    }

    @Override
    public Product find(int index) {
        return stock.get(index);
    }

    @Override
    public Product findByLabel(String label) {

        return stock.stream()
                .filter(p -> p.getLabel().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

    }

    @Override
    public Iterable<Product> findFirstByAlphabeticalOrder(int count) {
        if (count > stock.size() || count <= 0) {
            return new ArrayList<Product>();
        }

        return stock.stream()
                .sorted(Comparator.comparing(Product::getLabel))
                .limit(count)
                .collect(Collectors.toList());

    }

    @Override
    public Iterable<Product> findAllInRange(double lo, double hi) {

        return null;

    }

    @Override
    public Iterable<Product> findAllByPrice(double price) {

        return null;

    }

    @Override
    public Iterable<Product> findFirstMostExpensiveProducts(int count) {

        return null;

    }

    @Override
    public Iterable<Product> findAllByQuantity(int quantity) {

        return null;

    }

    @Override
    public Iterator<Product> iterator() {

        return null;

    }

}
