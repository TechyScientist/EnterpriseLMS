package com.johnnyconsole.libraryms.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;
import com.johnnyconsole.libraryms.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("EditPatronServlet")
public class EditPatronServlet extends HttpServlet {

    @EJB
    private UserDao userDao;
    
    @EJB
    private BookDao bookDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            String referrer = request.getHeader("referer");
            if(request.getParameter("edit-patron-submit") != null) {
                User user = (User) session.getAttribute("user");
                if(user.libraryStaff || user.libraryAdmin || request.getParameter("barcode").equals(user.barcode)) {
                    String barcode = request.getParameter("barcode"),
                            lastName = request.getParameter("last-name"),
                            firstName = request.getParameter("first-name"),
                            username = request.getParameter("username").toLowerCase(),
                            password = request.getParameter("password"),
                            confirmPassword = request.getParameter("confirm-password");
                    boolean staff = Integer.parseInt(request.getParameter("staff")) == 1,
                            admin = Integer.parseInt(request.getParameter("admin")) == 1;
                    int checkoutLimit = Integer.parseInt(request.getParameter("checkout-limit")),
                            loanTime = Integer.parseInt(request.getParameter("loan-time"));

                    if(barcodeBean.isValidPatronBarcode(barcode)) {
                        User patron = userDao.findByBarcode(barcode);
                        if(patron != null) {
                            patron.lastName = lastName;
                            patron.firstName = firstName;
                            patron.libraryStaff = staff;
                            patron.libraryAdmin = admin;
                            if(!username.equals(patron.username) && userDao.findByUsername(username) == null) {
                                patron.username = username.toLowerCase();
                            }
                            else if(!username.equals(patron.username)) {
                                session.setAttribute("patron", patron);
                                session.setAttribute("status", SC_PRECONDITION_FAILED);
                                response.sendRedirect(referrer);
                                return;
                            }
                            
                            if(bookDao.checkedOutBy(patron.barcode).size() <= checkoutLimit) {
                                patron.checkoutLimit = checkoutLimit;
                            }
                            else {
                                session.setAttribute("patron", patron);
                                session.setAttribute("status", SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                                response.sendRedirect(referrer);
                                return;
                            }
                            patron.loanTime = loanTime;
                            
                            if(!password.isEmpty()) {
                                if(confirmPassword.equals(password)) {
                                    patron.setPassword(BCrypt.with(BCrypt.Version.VERSION_2A)
                                            .hashToString(12, password.toCharArray()));
                                } else {
                                    session.setAttribute("patron", patron);
                                    session.setAttribute("status", SC_CONFLICT);
                                    response.sendRedirect(referrer);
                                    return;
                                }
                            }
                            userDao.update(patron);
                            if(patron.barcode.equals(user.barcode)) session.setAttribute("user", patron);
                            session.setAttribute("status", SC_ACCEPTED);
                            response.sendRedirect(referrer);
                        } else {
                            session.setAttribute("status", SC_NOT_FOUND);
                            response.sendRedirect(referrer);
                        }
                    } else {
                        session.setAttribute("status", SC_NOT_ACCEPTABLE);
                        response.sendRedirect(referrer);
                    }
                } else {
                    session.setAttribute("status", SC_FORBIDDEN);
                    response.sendRedirect("/library/dashboard.jsp");
                }
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect(referrer);
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
