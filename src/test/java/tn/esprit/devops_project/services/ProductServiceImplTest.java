package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tn.esprit.devops_project.entities.ProductCategory.ELECTRONICS;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class ProductServiceImplTest {
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    void addProduct() {
        Product product = new Product();
        product.setTitle("ORANGE");
        final Stock stock = this.stockService.retrieveStock(1L);
        product.setStock(stock);
        productService.addProduct(product,stock.getIdStock());
        final List<Product> AllProduct = this.productService.retreiveAllProduct();
        assertEquals(2, AllProduct.size());
        final Product produit = this.productService.retrieveProduct(2L);
        assertEquals("ORANGE", produit.getTitle());
    }

    @DatabaseSetup("/data-set/product-data.xml")
@DatabaseSetup("/data-set/stock-data.xml")
@Test
void retrieveProduct() {
    final Product product = this.productService.retrieveProduct(1L);
    assertNotNull(product);
    assertEquals(1L, product.getIdProduct());
    assertEquals("t1", product.getTitle());
}

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveAllProduct() {
        final List<Product> AllProduct = this.productService.retreiveAllProduct();
        assertEquals(1, AllProduct.size());
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void retrieveProductByCategory() {
        // Specify the category you want to test
        ProductCategory categoryToTest = ProductCategory.ELECTRONICS;

        // Retrieve products by the specified category
        List<Product> productsByCategory = this.productService.retrieveProductByCategory(categoryToTest);

        // Ensure that the list is not null
        assertNotNull(productsByCategory);

        // Perform assertions on the retrieved products, such as checking their category.
        // Example:
        for (Product product : productsByCategory) {
            assertEquals(categoryToTest, product.getCategory());
        }
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")

    void deleteProduct() {
        final Product product = this.productService.retrieveProduct(1L);
        productService.deleteProduct(product.getIdProduct());


    }

@Test
@DatabaseSetup({"/data-set/product-data.xml", "/data-set/stock-data.xml"})
void retrieveProductStock() {
    // Arrange
    Long stockId = 1L; // Assuming this is a valid ID in your dataset

    // Act
    List<Product> products = productService.retreiveProductStock(stockId);

    // Assert
    assertNotNull(products);
    assertEquals(0, products.size()); // Assuming you expect 0 products
}


    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Product product = this.productService.retrieveProduct(100L);
        });
    }
}
