package com.johnnyconsole.libraryms.persistence.implementations;

import com.johnnyconsole.libraryms.persistence.Book;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class BookDaoImpl implements BookDao {

    @PersistenceContext(unitName="book")
    private EntityManager manager;

    @Override
    public Book findByCopyCode(String barcode) {
        try {
            return (Book) manager.createNamedQuery("Book.SearchByCopyBarcode")
                    .setParameter("barcode", barcode)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Book> findByTitleBarcode(String barcode) {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.SearchByTitleBarcode")
                    .setParameter("barcode", barcode)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.SearchByTitle")
                    .setParameter("criteria", title)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> findByAuthor(String author) {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.SearchByAuthor")
                    .setParameter("criteria", author)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> checkedOutBy(String patronBarcode) {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.CheckedOutByPatron")
                    .setParameter("patron", patronBarcode)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> listAvailable() {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.ListAvailable")
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> listCheckedOut() {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.ListCheckedOut")
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Book> listLost() {
        try {
            return (List<Book>)  manager.createNamedQuery("Book.ListLost")
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
