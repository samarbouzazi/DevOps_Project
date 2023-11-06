package tn.esprit.devops_project.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.InvoiceDetail;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.repositories.InvoiceDetailRepository;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.repositories.OperatorRepository;
import tn.esprit.devops_project.repositories.SupplierRepository;
import tn.esprit.devops_project.services.Iservices.IInvoiceService;
import tn.esprit.devops_project.services.ProductServiceImpl;


import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InvoiceDetailService {

	

	final InvoiceDetailRepository invoiceDetailRepository;
	final ProductServiceImpl productServiceImpl;


    public void createInvoiceDetailWithExistingProduct(InvoiceDetail invoiceDetail, Long productId) {
        // Check if the product exists
        Product product = productServiceImpl.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException();
        }

        // Set the product on the invoice detail
        invoiceDetail.setProduct(product);

        // Save the invoice detail
        invoiceDetailRepository.save(invoiceDetail);
    }

    public void updateInvoiceDetailQuantity(InvoiceDetail invoiceDetail, int newQuantity) {
        invoiceDetail.setQuantity(newQuantity);

        // Calculate the new total price
        float totalPrice = invoiceDetail.getPrice() * invoiceDetail.getQuantity();
        invoiceDetail.setTotalPrice(totalPrice);

        // Save the updated invoice detail
        invoiceDetailRepository.save(invoiceDetail);
    }

    public float calculateInvoiceDetailTotalPrice(InvoiceDetail invoiceDetail) {
        return invoiceDetail.getPrice() * invoiceDetail.getQuantity();
    }
}
