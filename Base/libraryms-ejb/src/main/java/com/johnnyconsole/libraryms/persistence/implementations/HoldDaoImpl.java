package com.johnnyconsole.libraryms.persistence.implementations;

import com.johnnyconsole.libraryms.persistence.Hold;
import com.johnnyconsole.libraryms.persistence.interfaces.HoldDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class HoldDaoImpl implements HoldDao {

    @PersistenceContext(unitName="hold")
    private EntityManager manager;

    @Override
    public boolean place(Hold hold) {
        try {
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
