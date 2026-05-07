package com.johnnyconsole.libraryms.persistence.implementations;

import com.johnnyconsole.libraryms.persistence.Title;
import com.johnnyconsole.libraryms.persistence.interfaces.TitleDao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SuppressWarnings("unchecked")
@Stateless
public class TitleDaoImpl implements TitleDao {

    @PersistenceContext(unitName="title")
    private EntityManager manager;

    @Override
    public boolean create(Title title) {
        try {
            manager.persist(title);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean delete(Title title) {
        try {
            manager.remove(manager.contains(title) ? title : manager.merge(title));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Title findByBarcode(String barcode) {
        try {
            return (Title) manager.createNamedQuery("Title.FindByBarcode")
                    .setParameter("barcode", barcode)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }


}
