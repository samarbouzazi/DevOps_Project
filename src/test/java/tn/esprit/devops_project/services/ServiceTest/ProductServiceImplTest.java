package tn.esprit.devops_project.services.ServiceTest;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.controllers.OperatorController;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;
import tn.esprit.devops_project.repositories.StockRepository;
import tn.esprit.devops_project.services.ProductServiceImpl;
import tn.esprit.devops_project.services.StockServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")

public class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private StockServiceImpl stockService;





    // @DatabaseSetup("/data-set/product-data.xml")
    // @DatabaseSetup("/data-set/stock-data.xml")
    // @Test
    // void addProduct() {
    //     final Product product = new Product();
    //     Stock stock = this.stockService.retrieveStock(1L);
    //     product.setStock(stock);
    //     // product.setIdProduct(2L);
    //     product.setTitle("t2");
    //     product.setPrice(2);
    //     product.setQuantity(2);
    //     product.setCategory(ProductCategory.ELECTRONICS);
    //     this.productService.addProduct(product, stock.getIdStock());
    //     assertEquals(2, this.productService.retreiveAllProduct().size());
    // }

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
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct() {
        final Product product = this.productService.retrieveProduct(1L);
        assertEquals("t1", product.getTitle());
    }
    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProduct_NullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Product product = this.productService.retrieveProduct(100L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveAllProduct() {
        final List<Product> allProducts = this.productService.retreiveAllProduct();
        assertEquals(1, allProducts.size());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProductByCategory() {
        final List<Product> electronicsProducts = this.productService.retrieveProductByCategory(ProductCategory.ELECTRONICS);
        assertEquals(1, electronicsProducts.size());
    }

    @DatabaseSetup("/data-set/product-data.xml")
    @DatabaseSetup("/data-set/stock-data.xml")
    @Test
    void retrieveProductStock() {
        Stock stock = this.stockService.retrieveStock(1L);
        final List<Product> allProducts = this.productService.retreiveProductStock(2L);
        for (Product product : allProducts){
            assertEquals(product.getStock(),stock);
        }
    }
    @DatabaseSetup("/data-set/product-data.xml")
    @Test
    void deleteOperator() {
        Product product = this.productService.retrieveProduct(1L);
        this.productService.deleteProduct(product.getIdProduct());
    }

}
