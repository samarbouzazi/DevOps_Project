package tn.esprit.devops_project.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

//import static org.junit.jupiter.api.Assertions.*;


//import org.junit.Assert;
//import org.junit.Test;


@RunWith(SpringRunner.class)

public class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock private InvoiceDetailRepository invoiceDetailRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        invoiceService = new InvoiceServiceImpl(invoiceRepository,operatorRepository,invoiceDetailRepository,supplierRepository );
    }

    @Test
    public void testRetrieveAllInvoices() {
        // Arrange
        List<Invoice> invoices = new ArrayList<>();
        Invoice invoice = new Invoice();
        invoice.setIdInvoice(10L);
        invoices.add(invoice);

        when(invoiceRepository.findAll()).thenReturn(invoices);

        // Act
        List<Invoice> result = invoiceService.retrieveAllInvoices();


        invoices.forEach(x-> System.out.println("iii"+x.getIdInvoice()));

        result.forEach(x-> System.out.println("rrr"+x.getIdInvoice()));
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
        verify(invoiceRepository).save(invoice);
        assertEquals(true, invoice.getArchived());
    }

    @Test(expected = NullPointerException.class)
    public void testCancelInvoiceNotFound() {
        // Arrange
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act
        invoiceService.cancelInvoice(invoiceId);

        // No assertion needed, expects NullPointerException
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

    @Test(expected = NullPointerException.class)
    public void testRetrieveInvoiceNotFound() {
        // Arrange
        Long invoiceId = 1L;
        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        // Act
        invoiceService.retrieveInvoice(invoiceId);

        // No assertion needed, expects NullPointerException
    }

//    @Test
//    public void testGetInvoicesBySupplier() {
//        // Arrange
//        Long supplierId = 1L;
//        Supplier supplier = new Supplier();
//        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
//        List<Invoice> supplierInvoices = new ArrayList<>();
//        when(supplier.getInvoices()).thenReturn((Set<Invoice>) supplierInvoices);
//
//        // Act
//        List<Invoice> result = invoiceService.getInvoicesBySupplier(supplierId);
//
//        // Assert
//        assertEquals(supplierInvoices, result);
//    }

    @Test(expected = NullPointerException.class)
    public void testGetInvoicesBySupplierNotFound() {
        // Arrange
        Long supplierId = 1L;
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        // Act
        invoiceService.getInvoicesBySupplier(supplierId);

        // No assertion needed, expects NullPointerException
    }

   @Override
public void assignOperatorToInvoice(Long idOperator, Long idInvoice) {
    Invoice invoice = invoiceRepository.findById(idInvoice)
            .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Invoice not found"));
    Operator operator = operatorRepository.findById(idOperator)
            .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Operator not found"));
    operator.getInvoices().add(invoice);
    operatorRepository.save(operator);
}


    // Write similar tests for the remaining methods.

@Test
public void testGetTotalAmountInvoiceBetweenDates() {
    Date startDate = new Date(123, 10, 1); // Adjust year, month, and day as needed
    Date endDate = new Date(123, 10, 5);   // Adjust year, month, and day as needed

    // Mock the behavior of invoiceRepository.getTotalAmountInvoiceBetweenDates
    when(invoiceRepository.getTotalAmountInvoiceBetweenDates(startDate, endDate)).thenReturn(200f);

    float totalAmount = invoiceService.getTotalAmountInvoiceBetweenDates(startDate, endDate);
    assertEquals(200f, totalAmount, 0.01); // Adjust the delta value as needed
}

}


