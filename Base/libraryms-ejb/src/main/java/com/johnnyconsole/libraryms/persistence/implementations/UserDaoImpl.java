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
    public boolean create(User user) {
        try {
            manager.persist(user);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            manager.merge(user);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    @Override
    public boolean delete(User user) {
        try {
            manager.remove(manager.contains(user) ? user : manager.merge(user));
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    @Override
    public User findByBarcode(String barcode) {
        try {
           return (User) manager.createNamedQuery("User.FindByBarcode")
                    .setParameter("barcode", barcode)
                    .getSingleResult();
        } catch(Exception ex) {
            return null;
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            return (User) manager.createNamedQuery("User.FindByUsername")
                    .setParameter("username", username.toLowerCase())
                    .getSingleResult();
        } catch(Exception ex) {
            return null;
        }
    }
}
