package com.johnnyconsole.libraryms.interfaces;

import javax.ejb.Local;

@Local
public interface BarcodeBean {
    boolean isValid(String barcode);
    String generatePatronBarcode();
    String generateCopyBarcode();
}
