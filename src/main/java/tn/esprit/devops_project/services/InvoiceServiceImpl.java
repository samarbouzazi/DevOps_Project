package tn.esprit.devops_project.services;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Test
    public void testRetrieveAllInvoices() {
        // Arrange
        List<Invoice> invoices = new ArrayList<>();
        invoices.add(new Invoice());
        when(invoiceRepository.findAll()).thenReturn(invoices);

        // Act
        List<Invoice> result = invoiceService.retrieveAllInvoices();

        // Assert
        assertEquals(invoices, result);
    }

    @Test
    public void testCancelInvoice() {
        // Arrange
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        // Act
        invoiceService.cancelInvoice(invoiceId);

        // Assert
        assertTrue(invoice.getArchived());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testCancelInvoiceNotFound() {
        // Arrange
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act
        invoiceService.cancelInvoice(invoiceId);
    }

    @Test
    public void testRetrieveInvoice() {
        // Arrange
        Long invoiceId = 1L;
        Invoice invoice = new Invoice();
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        // Act
        Invoice result = invoiceService.retrieveInvoice(invoiceId);

        // Assert
        assertEquals(invoice, result);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testRetrieveInvoiceNotFound() {
        // Arrange
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act
        invoiceService.retrieveInvoice(invoiceId);
    }

    @Test
    public void testGetInvoicesBySupplier() {
        // Arrange
        Long supplierId = 1L;
        Supplier supplier = new Supplier();
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        Invoice invoice1 = new Invoice();
        Invoice invoice2 = new Invoice();
        List<Invoice> invoices = Arrays.asList(invoice1, invoice2);
        supplier.setInvoices(new HashSet<>(invoices));

        // Act
        List<Invoice> result = invoiceService.getInvoicesBySupplier(supplierId);

        // Assert
        assertEquals(invoices, result);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetInvoicesBySupplierNotFound() {
        // Arrange
        Long supplierId = 1L;
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        // Act
        invoiceService.getInvoicesBySupplier(supplierId);
    }

    @Test
    public void testAssignOperatorToInvoice_Success() {
        // Arrange
        Long idOperator = 1L;
        Long idInvoice = 2L;

        Operator operator = new Operator();
        Invoice invoice = new Invoice();

        when(operatorRepository.findById(idOperator)).thenReturn(Optional.of(operator));
        when(invoiceRepository.findById(idInvoice)).thenReturn(Optional.of(invoice));

        // Act
        invoiceService.assignOperatorToInvoice(idOperator, idInvoice);

        // Assert
        assertTrue(operator.getInvoices().contains(invoice));
        verify(operatorRepository, times(1)).save(operator);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAssignOperatorToInvoice_InvoiceNotFound() {
        // Arrange
        Long idOperator = 1L;
        Long idInvoice = -1L;
        
        when(invoiceRepository.findById(idInvoice)).thenReturn(Optional.empty());

        // Act
        invoiceService.assignOperatorToInvoice(idOperator, idInvoice);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testAssignOperatorToInvoice_OperatorNotFound() {
        // Arrange
        Long idOperator = -1L;
        Long idInvoice = 2L;

        when(operatorRepository.findById(idOperator)).thenReturn(Optional.empty());

        // Act
        invoiceService.assignOperatorToInvoice(idOperator, idInvoice);
    }

    @Test
    public void testGetTotalAmountInvoiceBetweenDates() {
        // Arrange
        Date startDate = new Date(123, 10, 1); // Adjust year, month, and day as needed
        Date endDate = new Date(123, 10, 5);   // Adjust year, month, and day as needed

        // Mock the behavior of invoiceRepository.getTotalAmountInvoiceBetweenDates
        when(invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate)).thenReturn(200f);

        // Act
        float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);

        // Assert
        assertEquals(200f, totalAmount, 0.01); // Adjust the delta value as needed
    }
}
