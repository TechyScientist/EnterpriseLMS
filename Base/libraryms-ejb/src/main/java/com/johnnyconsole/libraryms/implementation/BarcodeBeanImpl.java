package com.johnnyconsole.libraryms.implementation;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;
import com.johnnyconsole.libraryms.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class BarcodeBeanImpl implements BarcodeBean {

    @EJB
    private UserDao userDao;
    @EJB
    private BookDao bookDao;

    private boolean isValid(String barcode) {
        if(barcode == null || barcode.length() != 13 || !barcode.matches("\\d{13}")) return false;
        int check = barcode.charAt(barcode.length() - 1) - '0', sum = 0;

        for(int i = 0; i < barcode.length() - 1; i++) {
            sum += i % 2 == 0 ? barcode.charAt(i) - '0' : 3 * (barcode.charAt(i) - '0');
        }

        sum = sum % 10 == 0 ? 0 : 10 - sum % 10;
        return sum == check;
    }

    @Override
    public boolean isValidCopyBarcode(String barcode) {
        return isValid(barcode) && !(barcode.startsWith("13870") || barcode.startsWith("978") || barcode.startsWith("979"));
    }

    @Override
    public boolean isValidTitleBarcode(String barcode) {
        return isValid(barcode) && (barcode.startsWith("978") || barcode.startsWith("979"));
    }

    @Override
    public boolean isValidPatronBarcode(String barcode) {
        return isValid(barcode) && barcode.startsWith("13870");
    }

    @Override
    public String generatePatronBarcode() {
        StringBuilder barcode;
        do {
            barcode = new StringBuilder("13870");
            while (barcode.length() < 12) {
                barcode.append((int) (Math.random() * 10));
            }
            barcode.append(calculateCheckDigit(barcode.toString()));
        } while(userDao.findByBarcode(barcode.toString()) != null);
        return barcode.toString();
    }

    @Override
    public String generateCopyBarcode() {
        StringBuilder barcode;
        do {
            barcode = new StringBuilder(String.valueOf(2 + (int) (Math.random() * 7)));
            while (barcode.length() < 12) {
                barcode.append((int) (Math.random() * 10));
            }
            barcode.append(calculateCheckDigit(barcode.toString()));
        } while(bookDao.findByCopyCode(barcode.toString()) != null);
        return barcode.toString();
    }

    private char calculateCheckDigit(String barcode) {
        int sum = 0;
        for(int i = 0; i < barcode.length(); i++) {
            sum += i % 2 == 0 ? barcode.charAt(i) - '0' : 3 * (barcode.charAt(i) - '0');
        }

        sum = sum % 10 == 0 ? 0 : 10 - sum % 10;
        return (char)(sum + '0');
    }
}
