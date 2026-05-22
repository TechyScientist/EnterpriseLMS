package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.Fine;

import javax.ejb.Local;
import java.sql.Timestamp;
import java.util.List;

@Local
public interface FineDao {
    boolean assess(Fine fine);
    boolean remove(Fine fine);
    List<Fine> listForPatron(String patronBarcode);
    Fine search(String patronBarcode, Timestamp timestamp);
}
