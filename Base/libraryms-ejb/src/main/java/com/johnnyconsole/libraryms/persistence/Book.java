package com.johnnyconsole.libraryms.persistence;

import javax.persistence.*;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="libraryms_books")
@NamedQueries({
        @NamedQuery(name="Book.SearchByCopyBarcode", query="SELECT b FROM Book b WHERE b.copyBarcode=:barcode"),
        @NamedQuery(name="Book.SearchByTitleBarcode", query="SELECT b FROM Book b WHERE b.titleBarcode=:barcode"),
        @NamedQuery(name="Book.SearchByTitle", query="SELECT b FROM Book b JOIN Title t ON b.titleBarcode = t.titleBarcode WHERE t.title LIKE :criteria"),
        @NamedQuery(name="Book.SearchByAuthor", query="SELECT b FROM Book b JOIN Title t ON b.titleBarcode = t.titleBarcode WHERE t.author LIKE :criteria"),
        @NamedQuery(name="Book.CheckedOutByPatron", query="SELECT b FROM Book b WHERE b.status='Checked Out' AND b.outTo=:patron"),
        @NamedQuery(name="Book.OnHoldForPatron", query="SELECT b FROM Book b WHERE b.status='On Hold' AND b.outTo=:patron"),
        @NamedQuery(name="Book.ListAvailable", query="SELECT b FROM Book b WHERE b.status='Available'"),
        @NamedQuery(name="Book.ListCheckedOut", query="SELECT b FROM Book b WHERE b.status='Checked Out'"),
        @NamedQuery(name="Book.ListLost", query="SELECT b FROM Book b WHERE b.status='Lost'")
})
public class Book {

    @Id
    public String copyBarcode;
    public String titleBarcode, status, note, outTo;
    public Date dueDate;

    public Book() {

    }

    public Book(String copyBarcode, String titleBarcode) {
        this(copyBarcode, titleBarcode, null);
    }

    public Book(String copyBarcode, String titleBarcode, String note) {
        this.copyBarcode = copyBarcode;
        this.titleBarcode = titleBarcode;
        this.status = "Available";
        this.note = note;
    }

    //TODO: Remove this method -- used only for testing
    @Override
    public String toString() {
        return new StringBuilder()
                .append("Book {\n\tcopyBarcode: ").append(copyBarcode)
                .append("\n\ttileBarcode: ").append(titleBarcode)
                .append("\n\tstatus: ").append(status)
                .append(note != null ? "\n\tnote: " : "").append(note != null ? note : "")
                .append(status.equals("Checked Out") ? "\n\t\toutTo: " : "").append(status.equals("Checked Out") ? outTo : "")
                .append(status.equals("Checked Out") ? "\n\t\tdueDate: " : "").append(status.equals("Checked Out") ? dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "")
                .append("\n}").toString();
    }
}
