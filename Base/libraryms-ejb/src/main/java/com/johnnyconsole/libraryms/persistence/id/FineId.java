package com.johnnyconsole.libraryms.persistence.id;

import java.io.Serializable;
import java.sql.Timestamp;

public class FineId implements Serializable {

    Timestamp added;
    private String patron;

    public FineId() {

    }

    public FineId(Timestamp added, String patron) {
        this.added = added;
        this.patron = patron;
    }

}
