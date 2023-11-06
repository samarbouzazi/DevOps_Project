package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.*;
import tn.esprit.devops_project.exceptions.ProductNotFoundException;
import tn.esprit.devops_project.repositories.*;
import tn.esprit.devops_project.services.*;


@Service
@Slf4j
@AllArgsConstructor
public class InvoiceDetailService {

    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductServiceImpl productService; // Assuming ProductService interface is used

    // Assuming you have ProductService interface with a findById method
    public void createInvoiceDetailWithExistingProduct(InvoiceDetail invoiceDetail, Long productId) {
        Product product = productService.findById(productId);

        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found");
        }

        invoiceDetail.setProduct(product);
        invoiceDetailRepository.save(invoiceDetail);

        // Update the associated invoice's total price
        updateInvoiceTotalPrice(invoiceDetail);
    }

    public void updateInvoiceDetailQuantity(InvoiceDetail invoiceDetail, int newQuantity) {
        invoiceDetail.setQuantity(newQuantity);

        float totalPrice = invoiceDetail.getPrice() * invoiceDetail.getQuantity();
        invoiceDetail.setTotalPrice(totalPrice);

        updateInvoiceTotalPrice(invoiceDetail);

        invoiceDetailRepository.save(invoiceDetail);
    }

    public float calculateInvoiceDetailTotalPrice(InvoiceDetail invoiceDetail) {
        return invoiceDetail.getPrice() * invoiceDetail.getQuantity();
    }

    private void updateInvoiceTotalPrice(InvoiceDetail invoiceDetail) {
        Invoice invoice = invoiceDetail.getInvoice();
        float totalInvoicePrice = 0;

        for (InvoiceDetail detail : invoice.getInvoiceDetails()) {
            totalInvoicePrice += detail.getTotalPrice();
        }

        invoice.setTotalPrice(totalInvoicePrice);
        // Assuming you have an InvoiceRepository for saving invoices
        // invoiceRepository.save(invoice); 
    }
}
