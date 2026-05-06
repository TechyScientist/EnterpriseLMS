package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.Book;

import javax.ejb.Local;
import java.util.List;

@Local
public interface BookDao {
    Book findByCopyCode(String barcode);
    List<Book> findByTitleBarcode(String username);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> checkedOutBy(String patronBarcode);
    List<Book> listAvailable();
    List<Book> listCheckedOut();
    List<Book> listLost();
}
