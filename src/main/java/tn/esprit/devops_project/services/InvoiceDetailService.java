public class InvoiceDetailService {

    private InvoiceDetailRepository invoiceDetailRepository;

    public InvoiceDetailService(InvoiceDetailRepository invoiceDetailRepository) {
        this.invoiceDetailRepository = invoiceDetailRepository;
    }

    public void createInvoiceDetailWithExistingProduct(InvoiceDetail invoiceDetail, Long productId) {
        // Check if the product exists
        Product product = productService.findById(productId);
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
