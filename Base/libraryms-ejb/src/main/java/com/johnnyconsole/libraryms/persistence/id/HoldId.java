package com.johnnyconsole.libraryms.persistence.id;

import java.io.Serializable;

public class HoldId implements Serializable {

    private final String patronBarcode, titleBarcode;

    public HoldId(String patronBarcode, String titleBarcode) {
        this.patronBarcode = patronBarcode;
        this.titleBarcode = titleBarcode;
    }

}
