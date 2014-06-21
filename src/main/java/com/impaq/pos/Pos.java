package com.impaq.pos;

import com.impaq.pos.listeners.BarcodeScanListener;
import com.impaq.pos.model.ProductsReceipt;
import com.impaq.pos.dao.ProductDao;
import com.impaq.pos.devices.output.Display;
import com.impaq.pos.devices.output.Printer;
import com.impaq.pos.model.Product;
import com.impaq.pos.utils.MessageUtils;

import java.util.Optional;

public class Pos implements BarcodeScanListener {

    public static final String INVALID_BAR_CODE = "Invalid bar-code";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
    public static final String EXIT_CODE = "exit";

    private ProductDao productDao;
    private Display display;
    private Printer printer;
    private ProductsReceipt receipt = getNewProductReceipt();

    public Pos(ProductDao productDao, Display display, Printer printer) {

        this.productDao = productDao;
        this.display = display;
        this.printer = printer;
    }

    public void onBarcodeScan(String barcode) {
        if (isInvalidCode(barcode)) {
            handleInvalidCode();
        } else {
            handleValidCode(barcode);
        }
    }

    private void handleValidCode(String barcode) {
        if (isExitCode(barcode)) {
            handleExitCode();
        } else {
            Optional<Product> product = getProductByBarcode(barcode);
            handleScannedProduct(product);
        }
    }

    private Optional<Product> getProductByBarcode(String barcode) {
        return Optional.ofNullable(productDao.getByBarcode(barcode));
    }

    private void handleScannedProduct(Optional<Product> product) {
        if (product.isPresent()) {
            handleProductFound(product.get());
        } else {
            handleProductNotFound();
        }
    }

    private void handleProductNotFound() {
        display.showMessage(PRODUCT_NOT_FOUND);
    }

    private void handleProductFound(Product product) {
        String message = MessageUtils.getProductMessage(product);
        receipt.add(product);
        display.showMessage(message);
    }

    private void handleInvalidCode() {
        display.showMessage(INVALID_BAR_CODE);
    }

    private boolean isInvalidCode(String barcode) {
        return barcode == null || barcode.isEmpty();
    }

    private boolean isExitCode(String barcode) {
        return EXIT_CODE.equals(barcode);
    }

    public void handleExitCode() {
        receipt.getAll().stream().forEach(product -> printer.printLine(MessageUtils.getProductMessage(product)));

        Double sum = receipt.getSum();
        printer.printLine(MessageUtils.getTotalSumMessage(sum));
        display.showMessage(MessageUtils.getTotalSumMessage(sum));
        receipt = getNewProductReceipt();
    }

    private ProductsReceipt getNewProductReceipt() {
        return new ProductsReceipt();
    }

}
