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
import tn.esprit.devops_project.entities.Stock;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class StockServiceImplTest {

    @Mock
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addStock() {
        Stock stock = new Stock();
        stock.setTitle("titre1");

        stockService.addStock(stock);

        final List<Stock> allStocks = stockService.retrieveAllStock();
        assertEquals(2, allStocks.size());

        final Stock stock1 = stockService.retrieveStock(2L);
        assertEquals("titre1", stock1.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock() {
        Stock stock = new Stock();
        stock.setTitle("stock 1");

        when(stockService.retrieveStock(1L)).thenReturn(stock);

        final Stock retrievedStock = stockService.retrieveStock(1L);
        assertNotNull(retrievedStock);
        assertEquals("stock 1", retrievedStock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllStock() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("stock 1"));

        when(stockService.retrieveAllStock()).thenReturn(stocks);

        final List<Stock> retrievedStocks = stockService.retrieveAllStock();
        assertNotNull(retrievedStocks);
        assertEquals(1, retrievedStocks.size());
        assertEquals("stock 1", retrievedStocks.get(0).getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void updateStock() {
        Stock stock = new Stock();
        stock.setTitle("stock 1");
        stock.setIdStock(1L);

        when(stockService.retrieveStock(1L)).thenReturn(stock);

        stock.setTitle("UpdatedTitle");

        stockService.updateStock(stock);

        final Stock updatedStock = stockService.retrieveStock(1L);
        assertNotNull(updatedStock);
        assertEquals("UpdatedTitle", updatedStock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void deleteStock() {
        Stock stock = new Stock();
        stock.setTitle("stock 1");
        stock.setIdStock(1L);

        when(stockService.retrieveStock(1L)).thenReturn(stock);

        stockService.deleteStock(1L);

        assertNull(stockService.retrieveStock(1L));
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock_nullId() {
        assertThrows(NullPointerException.class, () -> {
            final Stock stock = stockService.retrieveStock(null);
        });
    }
}
