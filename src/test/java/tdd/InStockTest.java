package tdd;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class InStockTest {

    private ProductStock inStock;
    private Product product;
    private Product productTwo;

    @Before
    public void setUp() {

        this.inStock = new InStock();
        this.product = new Product("testLabel", 2, 5);
        this.productTwo = new Product("testLabelTwo", 1, 3);

    }

    @Test
    public void testAddInStockShouldContainProduct() {

        inStock.add(product);
        assertTrue(inStock.contains(product));

    }

    @Test
    public void testContainsShouldReturnFalseIfProductIsMissing() {

        assertFalse(inStock.contains(product));

    }

    @Test
    public void testGetCountShouldReturnCorrectNumberOfProductItemsInStock() {

        assertEquals(0, inStock.getCount());

        inStock.add(product);
        assertEquals(1, inStock.getCount());

        inStock.add(productTwo);
        assertEquals(2, inStock.getCount());

    }

    @Test
    public void testFindShouldReturnProductBasedOnIndexInInsertionOrder() {

        List<Product> productList = addMultipleProductsToStock();
        int index = 3;

        Product expected = productList.get(index);
        Product actual = inStock.find(index);

        assertSame(expected, actual);

    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindThrowsIndexOutOfBoundWhenInputEqualToStockSize() {

        List<Product> productList = addMultipleProductsToStock();
        inStock.find(productList.size());

    }

    @Test
    public void testChangeQuantityShouldUpdateTheQuantityOfAProduct() {


        inStock.add(product);

        int expected = product.getQuantity() + 3;
        inStock.changeQuantity(product.getLabel(), expected);

        int actual = product.getQuantity();

        assertEquals(expected, actual);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testChangeQuantityThrowsIllegalArgumentIfProductNotInStock() {

        inStock.changeQuantity(product.getLabel(), 1);

    }

    @Test
    public void testFindByLabelShouldReturnTheProductWithTheSameLabelAsInputArg() {

        addMultipleProductsToStock();
        inStock.add(product);

        Product actual = inStock.findByLabel(product.getLabel());

        assertNotNull(actual);
        assertEquals(product.getLabel(), actual.getLabel());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByLabelThrowsIllegalArgumentWhenNoMatchingIsFound() {

        inStock.findByLabel(product.getLabel());

    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnNProducts() {

        addMultipleProductsToStock();

        int expected = 5;

        List<Product> actual = iterableToList(inStock.findFirstByAlphabeticalOrder(expected));

        assertNotNull(actual);
        assertEquals(expected, actual.size());

    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnProductsSortedAlphabetically() {

        List<Product> productList = addMultipleProductsToStock().stream()
                .sorted(Comparator.comparing(Product::getLabel))
                .toList();

        int numberOfItems = productList.size();

        List<Product> actualList = iterableToList(inStock.findFirstByAlphabeticalOrder(numberOfItems));

        assertEquals(productList.size(), actualList.size());

//        for (int i = 0; i < actualList.size(); i++) {
//            String expected = productList.get(i).getLabel();
//            String actual = actualList.get(i).getLabel();
//            assertEquals(expected, actual);
//        }
        assertEquals(productList, actualList);

    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnEmptyCollectionIfNHigherThanProductsInStock() {

        int numberOfItems = addMultipleProductsToStock().size() + 1;

        List<Product> actual = iterableToList(inStock.findFirstByAlphabeticalOrder(numberOfItems));

        assertEquals(0, actual.size());

    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnEmptyCollectionIfNIsZero() {

        addMultipleProductsToStock();

        int numberOfItems = 0;

        List<Product> actual = iterableToList(inStock.findFirstByAlphabeticalOrder(numberOfItems));

        assertEquals(0, actual.size());

    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnEmptyCollectionIfNIsNegative() {

        addMultipleProductsToStock();

        int numberOfItems = -1;

        List<Product> actual = iterableToList(inStock.findFirstByAlphabeticalOrder(numberOfItems));

        assertEquals(0, actual.size());

    }

    @Test
    public void testFindAllInPriceRangeShouldReturnAllInRangeExclusiveLowerInclusiveUpper() {

        double lo = 1.00;
        double hi = 5.00;

        List<Product> productList = addMultipleProductsToStock().stream()
                .filter(p -> p.getPrice() > lo && p.getPrice() <= hi)
                .toList();

        List<Product> actualList = iterableToList(inStock.findAllInPriceRange(lo, hi));

        assertNotNull(actualList);

        assertEquals(productList.size(), actualList.size());

        boolean noPricesOutOfRange = actualList.stream()
                .map(Product::getPrice)
                .noneMatch(p -> p <= lo || p > hi);

        assertTrue(noPricesOutOfRange);

    }

    @Test
    public void testFindAllInPriceRangeShouldReturnAllInDescOrder() {

        double lo = 0.00;
        double hi = 100.00;

        List<Product> productList = addMultipleProductsToStock().stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .toList();

        List<Product> actualList = iterableToList(inStock.findAllInPriceRange(lo, hi));

        assertEquals(productList.size(), actualList.size());

//        for (int i = 0; i < productList.size(); i++) {
//            Product expected = productList.get(i);
//            Product actual = actualList.get(i);
//            assertEquals(expected, actual);
//        }
        assertEquals(productList, actualList);

    }

    @Test
    public void testFindAllInPriceRangeShouldReturnEmptyCollectionIfNoneFound() {

        double lo = 100.00;
        double hi = 500.00;

        addMultipleProductsToStock();

        List<Product> actualList = iterableToList(inStock.findAllInPriceRange(lo, hi));

        assertEquals(0, actualList.size());

    }

    @Test
    public void testFindAllByPriceShouldReturnAllItemsWithGivenPrice() {

        double price = 2.00;

        List<Product> priceList = addMultipleProductsToStock().stream()
                .filter(p -> p.getPrice() == price)
                .toList();

        List<Product> actualList = iterableToList(inStock.findAllByPrice(price));

        assertEquals(priceList.size(), actualList.size());

//        for (int i = 0; i < actualList.size(); i++) {
//            double expected = priceList.get(i).getPrice();
//            double actual = actualList.get(i).getPrice();
//            assertEquals(expected, actual, 0.00);
//        }
        assertEquals(priceList, actualList);

    }

    @Test
    public void testFindAllByPriceShouldReturnEmptyCollectionIfNoneFound() {

        addMultipleProductsToStock();

        double price = 200.00;

        List<Product> actualList = iterableToList(inStock.findAllByPrice(price));

        assertEquals(0, actualList.size());

    }

    @Test
    public void testFindFirstMostExpensiveProductsShouldReturnFirstNProducts() {

        int limit = 5;

        List<Product> productList = addMultipleProductsToStock().stream()
                .limit(limit)
                .toList();

        List<Product> actualList = iterableToList(inStock.findFirstMostExpensiveProducts(limit));

        assertEquals(productList.size(), actualList.size());

    }

    @Test
    public void testFindFirstMostExpensiveProductsShouldReturnOrderedByMostExpensiveProducts() {

        List<Product> productList = addMultipleProductsToStock().stream()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .toList();

        int limit = productList.size();

        List<Product> actualList = iterableToList(inStock.findFirstMostExpensiveProducts(limit));

        assertEquals(productList.size(), actualList.size());

//        for (int i = 0; i < actualList.size(); i++) {
//            Product expected = productList.get(i);
//            Product actual = actualList.get(i);
//            assertEquals(expected, actual);
//        }

        assertEquals(productList, actualList);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMostExpensiveProductsThrowsIllegalArgumentIfNLargerThanStock() {

        inStock.findFirstMostExpensiveProducts(1);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindFirstMostExpensiveProductsThrowsIllegalArgumentIfNIsNegative() {

        inStock.findFirstMostExpensiveProducts(-1);

    }

    @Test
    public void testFindAllByQuantityShouldReturnAllWithMatchingRemainingQuantity() {

        int quantity = 3;

        List<Product> productList = addMultipleProductsToStock().stream()
                .filter(p -> p.getQuantity() == quantity)
                .toList();

        List<Product> actualList = iterableToList(inStock.findAllByQuantity(quantity));

        assertEquals(productList.size(), actualList.size());

//        for (int i = 0; i < actualList.size(); i++) {
//            Product expected = productList.get(i);
//            Product actual = actualList.get(i);
//            assertEquals(expected, actual);
//        }

        assertEquals(productList, actualList);

    }

    @Test
    public void testFindAllByQuantityShouldReturnEmptyCollectionIfNoneFound() {

        int quantity = 300;

        addMultipleProductsToStock();

        List<Product> actualList = iterableToList(inStock.findAllByQuantity(quantity));

        assertEquals(0, actualList.size());

    }

    @Test
    public void testIteratorShouldReturnAllItemsInStock() {

        List<Product> productList = addMultipleProductsToStock();

        List<Product> actualList = iteratorToList(inStock.iterator());

        assertEquals(productList, actualList);

    }

    private List<Product> addMultipleProductsToStock() {

        List<Product> result = List.of(
                new Product("product_7", 7.00, 8),
                new Product("product_2", 4.00, 4),
                new Product("product_1", 3.00, 3),
                new Product("product_3", 6.00, 6),
                new Product("product_0", 1.00, 2),
                new Product("product_6", 4.00, 9),
                new Product("product_8", 2.00, 3),
                new Product("product_4", 8.00, 7),
                new Product("product_5", 1.00, 1),
                new Product("product_9", 9.00, 1)
        );

        result.forEach(inStock::add);

        return result;

    }

    private List<Product> iterableToList(Iterable<Product> source) {

        assertNotNull(source);
        List<Product> target = new ArrayList<>();
        source.forEach(target::add);

        return target;

    }

    private List<Product> iteratorToList(Iterator<Product> source) {

        assertNotNull(source);
        List<Product> result = new ArrayList<>();
        source.forEachRemaining(result::add);

        return result;

    }

}