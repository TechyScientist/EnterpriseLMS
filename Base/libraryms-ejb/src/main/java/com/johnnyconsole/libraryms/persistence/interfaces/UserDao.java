package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.User;

import javax.ejb.Local;

@Local
public interface UserDao {
    User findByBarcode(String barcode);
    User findByUsername(String username);
}
