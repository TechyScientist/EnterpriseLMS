package com.johnnyconsole.libraryms.persistence.interfaces;

import com.johnnyconsole.libraryms.persistence.Hold;

import javax.ejb.Local;
import java.util.List;

@Local
public interface HoldDao {
    boolean place(Hold hold);
    boolean remove(Hold hold);
    boolean pop(Hold hold);
    Hold retrieve(String patronBarcode, String titleBarcode);
    List<Hold> listByPatronBarcode(String patronBarcode);
    List<Hold> listByTitleBarcode(String titleBarcode);
    Hold nextForTitleBarcode(String titleBarcode);

}
