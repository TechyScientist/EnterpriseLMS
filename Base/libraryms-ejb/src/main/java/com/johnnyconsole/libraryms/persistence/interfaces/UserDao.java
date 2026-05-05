package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.User;

import javax.ejb.Local;

@Local
public interface UserDao {
    User getUserByBarcode(String barcode);
    User getUserByUsername(String username);
}
