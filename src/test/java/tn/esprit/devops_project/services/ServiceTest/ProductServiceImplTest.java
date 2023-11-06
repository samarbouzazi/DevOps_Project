package tn.esprit.devops_project.services.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.ProductRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
public class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private StockServiceImpl stockService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private StockRepository stockRepository;

    @Test
    public void testAddProduct_WithValidProductAndIdStock_ShouldReturnSavedProduct() {
        Product product = new Product("t1", "Product Description", 1.0, 1);
        Long idStock = 1L;

        Stock stock = new Stock(10);
        Mockito.when(stockRepository.findById(idStock)).thenReturn(Optional.of(stock));

        Product savedProduct = productServiceImpl.addProduct(product, idStock);

        assertEquals("t1", savedProduct.getTitle());
        assertEquals("Product Description", savedProduct.getDescription());
        assertEquals(1.0, savedProduct.getPrice(), 0.01);
        assertEquals(1, savedProduct.getQuantity());
        assertEquals("ELECTRONICS", savedProduct.getCategory());
        assertEquals(stock, savedProduct.getStock());

        Mockito.verify(productRepository).save(product);
    }

    @Test(expected = NullPointerException.class)
    public void testAddProduct_WithValidProductAndNullIdStock_ShouldThrowNullPointerException() {
        Product product = new Product("Product Name", "Product Description", 100.0, 1);
        Long idStock = null;

        productServiceImpl.addProduct(product, idStock);
    }

    @Test
    public void retrieveProduct() {
        Long id = 1L;
        Product product = new Product("t1", "Product Description", 1.0, 1);

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Product retrievedProduct = productServiceImpl.retrieveProduct(id);

        assertEquals(id, retrievedProduct.getId());
        assertEquals("t1", retrievedProduct.getTitle());
        assertEquals("Product Description", retrievedProduct.getDescription());
        assertEquals(1.0, retrievedProduct.getPrice(), 0.01);
        assertEquals(1, retrievedProduct.getQuantity());
        assertEquals("ELECTRONICS", retrievedProduct.getCategory());

        Mockito.verify(productRepository).findById(id);
    }

    @Test(expected = NullPointerException.class)
    public void retrieveProduct_NullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Product product = this.productService.retrieveProduct(100L);
        });
    }

    @Test
    public void retreiveAllProduct() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Product Name 1", "Product Description 1", 100.0, 1));
        products.add(new Product("Product Name 2", "Product Description 2", 150.0, 2));

        Mockito.when(productRepository.findAll()).thenReturn(products);

        List<Product> retrievedProducts = productServiceImpl.retreiveAllProduct();

        assertEquals(products.size(), retrievedProducts.size());
        for (int i = 0; i < retrievedProducts.size(); i++) {
            assertEquals(products.get(i).getId(), retrieved
