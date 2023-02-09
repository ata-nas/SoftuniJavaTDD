package tdd;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InStockTest {

    private ProductStock inStock;
    private Product product;
    private Product productTwo;

    @Before
    public void setUp(){

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

        assertEquals(expected,actual);

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

    /**
     * findFirstByAlphabeticalOrder(int n);
     *     case 1: return n products;
     *     case 2: return ordered by label;
     *     case 3: return empty collection if n out of range;
     */
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

        List<Product> productList = addMultipleProductsToStock()
                .stream()
                .sorted(Comparator.comparing(Product::getLabel))
                .toList();

        int numberOfItems = productList.size();

        List<Product> actualList = iterableToList(inStock.findFirstByAlphabeticalOrder(numberOfItems));

        for (int i = 0; i < actualList.size(); i++) {
            String expected = productList.get(i).getLabel();
            String actual = actualList.get(i).getLabel();
            assertEquals(expected, actual);
        }

    }

    @Test
    public void testFindFirstByAlphabeticalOrderShouldReturnEmptyCollectionIfNHigherThanProductsInStock() {

        int numberOfItems = addMultipleProductsToStock().size() + 1;

        List<Product> actual = iterableToList(inStock.findFirstByAlphabeticalOrder(numberOfItems));

        assertEquals(0, actual.size());

    }

    private List<Product> addMultipleProductsToStock() {

        List<Product> result = List.of(
                new Product("product_7", 7, 8),
                new Product("product_2", 4, 4),
                new Product("product_1", 3, 3),
                new Product("product_3", 6, 6),
                new Product("product_0", 1, 2),
                new Product("product_6", 4, 9),
                new Product("product_8", 2, 3),
                new Product("product_4", 8, 7),
                new Product("product_5", 1, 1),
                new Product("product_9", 9, 1)
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

}