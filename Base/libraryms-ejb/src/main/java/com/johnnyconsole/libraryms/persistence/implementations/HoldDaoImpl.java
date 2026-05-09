package com.johnnyconsole.libraryms.persistence.implementations;

import com.johnnyconsole.libraryms.persistence.Book;
import com.johnnyconsole.libraryms.persistence.Hold;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;
import com.johnnyconsole.libraryms.persistence.interfaces.HoldDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class HoldDaoImpl implements HoldDao {

    @PersistenceContext(unitName="hold")
    private EntityManager manager;

    @EJB
    private BookDao bookDao;

    @Override
    public boolean place(Hold hold) {
        try {
            if(retrieve(hold.patronBarcode, hold.titleBarcode) != null) return false;
            manager.persist(hold);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean remove(Hold hold) {
        try {
            manager.remove(manager.contains(hold) ? hold : manager.merge(hold));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean pop(Hold hold) {
        try {
            remove(hold);
            List<Book> holds = bookDao.onHoldFor(hold.patronBarcode);
            if(holds.isEmpty())  return true;
            else {
                for(Book book : holds) {
                    if (book.titleBarcode.equals(hold.titleBarcode)) {
                        Hold next = nextForTitleBarcode(hold.titleBarcode);
                        if(next == null) {
                            bookDao.checkIn(book);
                        }
                        else {
                            bookDao.hold(book, next.patronBarcode);
                        }
                        break;
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Hold retrieve(String patronBarcode, String titleBarcode) {
        try {
            return (Hold) manager.createNamedQuery("Hold.RetrieveHold")
                    .setParameter("patron", patronBarcode)
                    .setParameter("title", titleBarcode)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Hold> listByPatronBarcode(String patronBarcode) {
        try {
            return (List<Hold>) manager.createNamedQuery("Hold.PatronHoldList")
                    .setParameter("patron", patronBarcode)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Hold> listByTitleBarcode(String titleBarcode) {
        try {
            return (List<Hold>) manager.createNamedQuery("Hold.TitleHoldList")
                    .setParameter("title", titleBarcode)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }

    @Override
    public Hold nextForTitleBarcode(String titleBarcode) {
        List<Hold> holds = listByTitleBarcode(titleBarcode);
        return holds.isEmpty() ?  null : holds.get(0);
    }
}
