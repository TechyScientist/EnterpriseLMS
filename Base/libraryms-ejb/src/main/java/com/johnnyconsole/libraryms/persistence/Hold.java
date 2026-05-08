package com.johnnyconsole.libraryms.persistence;

import com.johnnyconsole.libraryms.persistence.id.HoldId;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="libraryms_holds")
@IdClass(HoldId.class)
@NamedQueries({
        @NamedQuery(name="Hold.PatronHoldList", query="SELECT h FROM Hold h WHERE h.patronBarcode=:patron ORDER BY h.placed"),
        @NamedQuery(name="Hold.TitleHoldList", query="SELECT h FROM Hold h WHERE h.titleBarcode=:title ORDER BY h.placed")
})
public class Hold {

    @Id
    public String patronBarcode, titleBarcode;
    @Column(insertable = false, updatable = false) public Timestamp placed;

    public Hold() {

    }

    public Hold(String patronBarcode, String titleBarcode) {
        this.patronBarcode = patronBarcode;
        this.titleBarcode = titleBarcode;
    }

    //TODO: Remove this method -- used only for testing
    @Override
    public String toString() {
        return new StringBuilder().append("Hold {")
                .append("\n\tpatronBarcode: ").append(patronBarcode)
                .append("\n\ttitleBarcode: ").append(titleBarcode)
                .append("\n\tplaced: ").append(placed.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy h:mm:ss a")))
                .append("\n}").toString();
    }
}
