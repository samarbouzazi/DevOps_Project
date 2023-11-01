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

import java.util.List;
import java.util.Set;

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
    InvoiceServiceImpl invoiceService;
    @Autowired
    StockServiceImpl stockService;
    @Autowired
    SupplierServiceImpl supplierService;
    @Autowired
    OperatorServiceImpl operatorService;



    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
        final List<Invoice> allInvoices = this.invoiceService.retrieveAllInvoices();
        assertEquals(allInvoices.size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        Long invoiceId = 1L; 


        invoiceService.cancelInvoice(invoiceId);

      
        final Invoice invoice = this.invoiceService.retrieveInvoice(invoiceId);
        assertNotNull(invoice);
        assertTrue(invoice.getArchived());
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
}
