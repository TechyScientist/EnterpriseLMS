package com.johnnyconsole.libraryms.persistence.implementations;

import com.johnnyconsole.libraryms.persistence.Fine;
import com.johnnyconsole.libraryms.persistence.interfaces.FineDao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
@Stateless
public class FineDaoImpl implements FineDao {

    @PersistenceContext(unitName = "fine")
    private EntityManager manager;

    @Override
    public boolean assess(Fine fine) {
        try {
            manager.persist(fine);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean remove(Fine fine) {
        try {
            manager.remove(manager.contains(fine) ? fine : manager.merge(fine));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Fine> listForPatron(String patronBarcode) {
        try {
            return (List<Fine) manager.createNamedQuery("Fine.ListForPatron")
                    .setParameter("patron", patronBarcode)
                    .getResultList();
        } catch (Exception ex) {
            return Collections.emptyList();
        }
    }
}
