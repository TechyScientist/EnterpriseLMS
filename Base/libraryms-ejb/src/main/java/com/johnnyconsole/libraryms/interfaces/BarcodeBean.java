package com.johnnyconsole.libraryms.interfaces;

import javax.ejb.Local;

@Local
public interface BarcodeBean {
    boolean isValidCopyBarcode(String barcode);
    boolean isValidTitleBarcode(String barcode);
    boolean isValidPatronBarcode(String barcode);
    String generatePatronBarcode();
    String generateCopyBarcode();
}
