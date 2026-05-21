package com.johnnyconsole.libraryms.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.*;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("AddPatronServlet")
public class AddPatronServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            if(request.getParameter("add-patron-submit") != null) {
                User user = (User) session.getAttribute("user");
                if(user.libraryStaff || user.libraryAdmin) {
                    String barcode = (request.getParameter("barcode") == null || request.getParameter("barcode").isEmpty())
                            ? barcodeBean.generatePatronBarcode() : request.getParameter("barcode"),
                            lastName = request.getParameter("last-name"),
                            firstName = request.getParameter("first-name"),
                            username = request.getParameter("username"),
                            password = request.getParameter("password"),
                            confirmPassword = request.getParameter("confirm-password");
                    boolean staff = Integer.parseInt(request.getParameter("staff")) == 1,
                            admin = Integer.parseInt(request.getParameter("admin")) == 1;

                    if(userDao.findByUsername(username) == null) {
                        if(password.equals(confirmPassword)) {
                            userDao.create(new User(barcode, username,
                                    BCrypt.with(BCrypt.Version.VERSION_2A).hashToString(12, password.toCharArray()),
                                    lastName, firstName, staff, admin));
                            session.setAttribute("status", SC_CREATED);
                            response.sendRedirect("/library/add-patron.jsp");
                        } else {
                            session.setAttribute("status", SC_CONFLICT);
                            response.sendRedirect("/library/add-patron.jsp");
                        }
                    } else {
                        session.setAttribute("status", SC_NOT_ACCEPTABLE);
                        response.sendRedirect("/library/add-patron.jsp");
                    }
                } else {
                    session.setAttribute("status", SC_FORBIDDEN);
                    response.sendRedirect("/library/dashboard.jsp");
                }
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/add-patron.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
