package com.johnnyconsole.libraryms.persistence;

import com.johnnyconsole.libraryms.persistence.id.FineId;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="libraryms_fines")
@IdClass(FineId.class)
@NamedQueries({
        @NamedQuery(name="Fine.ListForPatron", query="SELECT f FROM Fine f WHERE f.patron=:patron ORDER BY f.added")
})
public class Fine {

    @Id
    public String patron;
    @Id @Column(insertable = false, updatable = false) public Timestamp added;
    public double amount;
    public String note;

    public Fine() {

    }

    public Fine(String patron, double amount, String note) {
        this.patron = patron;
        this.amount = amount;
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    //TODO: Remove this method -- used only for testing
    @Override
    public String toString() {
        return new StringBuilder().append("Fine {")
                .append("\n\tadded: ").append(added.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy h:mm:ss a")))
                .append("\n\tpatron: ").append(patron)
                .append("\n\tamount ").append(String.format("$%.2f", amount))
                .append("\n}").toString();
    }
}
