package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.Book;

import javax.ejb.Local;
import javax.swing.*;
import java.util.List;

@Local
public interface BookDao {
    void create(Book book);
    void checkIn(Book book);
    void checkOut(Book book, String patronBarcode);
    void hold(Book book, String patronBarcode);
    void renew(Book book);
    void markLost(Book book);
    Book findByCopyCode(String barcode);
    List<Book> findByTitleBarcode(String username);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> checkedOutBy(String patronBarcode);
    List<Book> onHoldFor(String patronBarcode);
    List<Book> listAvailable();
    List<Book> listCheckedOut();
    List<Book> listLost();
}
