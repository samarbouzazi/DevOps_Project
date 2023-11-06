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
import tn.esprit.devops_project.entities.*;

import java.sql.Date;
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
class InvoiceServiceImplTest {
    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private OperatorServiceImpl operatorService;
    @Autowired
    private SupplierServiceImpl supplierService;
    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
        final List<Invoice> AllInvoices = this.invoiceService.retrieveAllInvoices();
        assertEquals(1, AllInvoices.size());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        invoiceService.cancelInvoice(invoice.getIdInvoice());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveInvoice() {
        final Invoice inv = this.invoiceService.retrieveInvoice(1L);
        assertNotNull(inv);
        assertEquals(1L, inv.getIdInvoice());

    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice_nullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Invoice invoice = this.invoiceService.retrieveInvoice(100L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    @DatabaseSetup("/data-set/operator-data.xml")
    void assignOperatorToInvoice() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        final Operator operateur = this.operatorService.retrieveOperator(1L);
        invoiceService.assignOperatorToInvoice(operateur.getIdOperateur(),invoice.getIdInvoice());
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates() {
        final Invoice invoice  = this.invoiceService.retrieveInvoice(1L);
        float expectedTotalAmount = 30.0f;
        float totalAmount = this.invoiceService.getTotalAmountInvoiceBetweenDates(
                invoice.getDateCreationInvoice(),invoice.getDateLastModificationInvoice());
        assertEquals(expectedTotalAmount, totalAmount, 0.01f); 
    }

   @Test
@DatabaseSetup({"/data-set/supplier-data.xml", "/data-set/invoice-data.xml"})
void getInvoicesBySupplier() {
    // Create a sample supplier
    Supplier supplier = new Supplier();
    supplier.setIdSupplier(1L);

    // Create a sample invoice and associate it with the supplier
    Invoice invoice = new Invoice();
    invoice.setSupplier(supplier);

    // Get the invoices by supplier
    List<Invoice> invoices = invoiceService.getInvoicesBySupplier(supplier.getIdSupplier());

    // Assert that the list of invoices is not null and contains the expected number of invoices
    assertNotNull(invoices);
    assertEquals(1, invoices.size()); // Adjust the expected size as needed
}



}
