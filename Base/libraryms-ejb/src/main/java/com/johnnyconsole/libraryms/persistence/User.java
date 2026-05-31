package com.johnnyconsole.libraryms.persistence;

import javax.persistence.*;

@Entity
@Table(name="libraryms_users")
@NamedQueries({
        @NamedQuery(name="User.FindByBarcode", query="SELECT u FROM User u WHERE u.barcode=:barcode"),
        @NamedQuery(name="User.FindByUsername", query="SELECT u FROM User u WHERE u.username=:username"),
        @NamedQuery(name="User.SearchByLastName", query="SELECT u FROM User u WHERE u.lastName LIKE '%:criteria%'")
})
public class User {

    @Id
    public String barcode;
    public String username, lastName, firstName;
    public boolean libraryStaff, libraryAdmin;
    public int checkoutLimit = 5, loanTime = 14;
    private String password;

    public User() {

    }

    public User(String barcode, String username, String password, String lastName, String firstName, boolean staff, boolean admin) {
        this.barcode = barcode;
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.libraryStaff = staff;
        this.libraryAdmin = admin;
    }

    public String getPassword() {
        return password;
    }

    //TODO: Remove this method -- used only for testing
    @Override
    public String toString() {
        return new StringBuilder()
                .append("User {\n\tbarcode: ").append(barcode)
                .append("\n\tlastName: ").append(lastName)
                .append("\n\tfirstName: ").append(firstName)
                .append("\n\tusername: ").append(username)
                .append("\n}").toString();
    }
}
