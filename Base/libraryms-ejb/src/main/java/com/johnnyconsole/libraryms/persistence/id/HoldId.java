package com.johnnyconsole.libraryms.persistence.id;

import java.io.Serializable;

public class HoldId implements Serializable {

    private String patronBarcode, titleBarcode;

    public HoldId() {

    }

    public HoldId(String patronBarcode, String titleBarcode) {
        this.patronBarcode = patronBarcode;
        this.titleBarcode = titleBarcode;
    }

}
