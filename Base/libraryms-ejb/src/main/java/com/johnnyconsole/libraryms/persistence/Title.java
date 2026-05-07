package com.johnnyconsole.libraryms.persistence;

import javax.persistence.*;

@Entity
@Table(name="libraryms_titles")
@NamedQueries(@NamedQuery(name="Title.FindByBarcode", query="SELECT t FROM Title t WHERE t.titleBarcode=:barcode"))
public class Title {

    @Id
    public String titleBarcode;
    public String title, author;

    public Title() {

    }

    public Title(String titleBarcode, String title, String author) {
        this.titleBarcode = titleBarcode;
        this.title = title;
        this.author = author;
    }

    //TODO: Remove this method -- used only for testing
    @Override
    public String toString() {
        return new StringBuilder()
                .append("Book {\n\ttileBarcode: ").append(titleBarcode)
                .append("\n\ttitle: ").append(title)
                .append("\n\tauthor: ").append(author)
                .append("\n}").toString();
    }
}
