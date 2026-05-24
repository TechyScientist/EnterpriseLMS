package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.Book;
import com.johnnyconsole.libraryms.persistence.Hold;
import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;
import com.johnnyconsole.libraryms.persistence.interfaces.HoldDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("CheckInServlet")
public class CheckInServlet extends HttpServlet {

    @EJB
    private BookDao bookDao;

    @EJB
    private HoldDao holdDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if(user.libraryStaff || user.libraryAdmin) {
                if (request.getParameter("checkin-submit") != null) {
                    String barcode = request.getParameter("copy-barcode");
                    if (barcodeBean.isValidCopyBarcode(barcode)) {
                        Book book = bookDao.findByCopyCode(barcode);
                        if (book == null) {
                            session.setAttribute("status", SC_NOT_FOUND);
                            response.sendRedirect("/library/staff.jsp");
                        } else if (!book.status.equals("Available")) { //TODO: Add check for if a book is waiting on hold
                            Hold hold = holdDao.nextForTitleBarcode(book.titleBarcode);
                            if (hold != null) {
                                session.setAttribute("status", SC_CONTINUE);
                                session.setAttribute("patron", hold.patronBarcode);
                                session.setAttribute("operation", "checkin");
                                bookDao.hold(book, hold.patronBarcode);
                                holdDao.remove(hold);
                            } else if (book.dueDate.before(Date.valueOf(LocalDate.now()))) {
                                bookDao.checkIn(book);
                                session.setAttribute("status", SC_PARTIAL_CONTENT);
                                session.setAttribute("due", book.dueDate);
                                session.setAttribute("operation", "checkin");
                            } else {
                                bookDao.checkIn(book);
                                session.setAttribute("status", SC_ACCEPTED);
                                session.setAttribute("operation", "checkin");
                            }
                            response.sendRedirect("/library/staff.jsp");
                        } else {
                            session.setAttribute("status", SC_CONFLICT);
                            session.setAttribute("operation", "checkin");
                            response.sendRedirect("/library/staff.jsp");
                        }

                    } else {
                        session.setAttribute("status", SC_BAD_REQUEST);
                        response.sendRedirect("/library/staff.jsp");
                    }
                }
            }
            else {
                session.setAttribute("status", SC_UNAUTHORIZED);        //TODO: Handle this error code on dashboard page
                response.sendRedirect("/library/dashboard.jsp");
            }
        }
        else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
