package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.Book;

import javax.ejb.Local;
import javax.swing.*;
import java.util.List;

@Local
public interface BookDao {
    boolean create(Book book);
    boolean delete(Book book);
    boolean checkIn(Book book);
    boolean checkOut(Book book, String patronBarcode);
    boolean hold(Book book, String patronBarcode);
    boolean renew(Book book);
    boolean markLost(Book book);
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
