@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class StockServiceImplTest {

    @Autowired
    private StockServiceImpl stockService;

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void addStock() {
        Stock stock = new Stock();
        stock.setTitle("titre1");
        stockService.addStock(stock);

        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        assertEquals(2, allStocks.size());

        final Stock stock1 = this.stockService.retrieveStock(2L);
        assertEquals("titre1", stock1.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock() {
        final Stock stock = this.stockService.retrieveStock(1L);
        assertNotNull(stock);
        assertEquals("stock 1", stock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveAllStock() {
        final List<Stock> allStocks = this.stockService.retrieveAllStock();
        assertEquals(1, allStocks.size());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void updateStock() {
        Stock stockToUpdate = this.stockService.retrieveStock(1L);
        stockToUpdate.setTitle("UpdatedTitle");
        stockService.updateStock(stockToUpdate);

        final Stock updatedStock = this.stockService.retrieveStock(1L);
        assertEquals("UpdatedTitle", updatedStock.getTitle());
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void deleteStock() {
        final Stock stock = this.stockService.retrieveStock(1L);
        stockService.deleteStock(stock.getIdStock());
        assertNull(this.stockService.retrieveStock(1L));
    }

    @Test
    @DatabaseSetup("/data-set/stock-data.xml")
    void retrieveStock_nullId() {
        assertThrows(NullPointerException.class, () -> {
            final Stock stock = this.stockService.retrieveStock(null);
        });
    }
}
