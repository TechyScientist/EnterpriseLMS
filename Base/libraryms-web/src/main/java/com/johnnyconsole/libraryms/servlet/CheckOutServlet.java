package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.Book;
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
import java.time.format.DateTimeFormatter;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("CheckOutServlet")
public class CheckOutServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    @EJB
    private BookDao bookDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if (request.getParameter("checkout-submit") != null) {
                String patronBarcode = request.getParameter("patron-barcode"),
                        copy = request.getParameter("copy-barcode"),
                        referrer = request.getParameter("referrer");
                if (barcodeBean.isValidCopyBarcode(copy) && barcodeBean.isValidPatronBarcode(patronBarcode)) {
                    Book book = bookDao.findByCopyCode(copy);
                    User patron = userDao.findByBarcode(patronBarcode);

                    if (book == null || patron == null) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect(referrer);
                    } else if (!book.status.equals("Available") && !book.status.equals("On Hold")) {
                        session.setAttribute("status", SC_CONFLICT);
                        session.setAttribute("operation", "checkout");
                        response.sendRedirect(referrer);
                    }
                    else if(book.status.equals("On Hold")) {
                        if(user.libraryStaff || user.libraryAdmin) {
                            if(patron.barcode.equals(book.outTo)) {
                                if(bookDao.checkedOutBy(patron.barcode).size() < patron.checkoutLimit) {
                                    bookDao.checkOut(book, patron);
                                    session.setAttribute("status", SC_ACCEPTED);
                                    session.setAttribute("operation", "checkout");
                                    session.setAttribute("due-date", book.dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
                                    response.sendRedirect(referrer);
                                } else {
                                    session.setAttribute("status", SC_LENGTH_REQUIRED);
                                    session.setAttribute("operation", "checkout");
                                    response.sendRedirect(referrer);
                                }
                            } else {
                                session.setAttribute("status", SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                                session.setAttribute("operation", "checkout");
                                response.sendRedirect(referrer);
                            }
                        }
                        else {
                            session.setAttribute("status", SC_NOT_ACCEPTABLE);
                            session.setAttribute("operation", "checkout");
                            response.sendRedirect(referrer);
                        }
                    } else {
                        if(bookDao.checkedOutBy(patron.barcode).size() < patron.checkoutLimit) {
                            bookDao.checkOut(book, patron);
                            session.setAttribute("status", SC_ACCEPTED);
                            session.setAttribute("operation", "checkout");
                            session.setAttribute("due-date", book.dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
                            response.sendRedirect(referrer);
                        } else {
                            session.setAttribute("status", SC_LENGTH_REQUIRED);
                            session.setAttribute("operation", "checkout");
                            response.sendRedirect(referrer);
                        }
                    }

                } else {
                    session.setAttribute("status", SC_BAD_REQUEST);
                    response.sendRedirect(referrer);
                }
            }
        }
        else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
