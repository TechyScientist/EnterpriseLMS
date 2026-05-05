package com.johnnyconsole.libraryms.persistence.implementations;

import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.UserDao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserDaoImpl implements UserDao {

    @PersistenceContext(unitName="user") private EntityManager manager;

    @Override
    public User getUserByBarcode(String barcode) {
        try {
           return (User) manager.createNamedQuery("User.FindByBarcode")
                    .setParameter("barcode", barcode)
                    .getSingleResult();
        } catch(Exception ex) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            return (User) manager.createNamedQuery("User.FindByUsername")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch(Exception ex) {
            return null;
        }
    }
}
