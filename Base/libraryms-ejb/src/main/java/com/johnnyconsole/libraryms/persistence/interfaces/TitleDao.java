package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.Title;

import javax.ejb.Local;

@Local
public interface TitleDao {
    boolean create(Title title);
    boolean delete(Title title);
    Title findByBarcode(String barcode);
}
