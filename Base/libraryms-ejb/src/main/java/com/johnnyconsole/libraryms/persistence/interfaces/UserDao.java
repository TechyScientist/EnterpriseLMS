package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.User;

import javax.ejb.Local;

@Local
public interface UserDao {
    boolean create(User user);
    boolean update(User user);
    boolean delete(User user);
    User findByBarcode(String barcode);
    User findByUsername(String username);
}
