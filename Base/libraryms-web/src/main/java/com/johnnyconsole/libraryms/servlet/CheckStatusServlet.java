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
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("CheckStatusServlet")
public class CheckStatusServlet extends HttpServlet {

    @EJB
    private BarcodeBean barcodeBean;

    @EJB
    private BookDao bookDao;

    @EJB
    private HoldDao holdDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(request.getSession().getAttribute("user") != null) {
            User user = (User) request.getSession().getAttribute("user");
            if (user.libraryStaff || user.libraryAdmin) {
                if (request.getParameter("status-check-submit") != null) {
                    session.setAttribute("operation", "status-check");
                    String barcode = request.getParameter("copy-barcode");
                    if (barcodeBean.isValidCopyBarcode(barcode)) {
                        Book book = bookDao.findByCopyCode(barcode);
                        List<Hold> holdList = holdDao.listByTitleBarcode(book.titleBarcode);
                        if (book.status.equals("On Hold") || !holdList.isEmpty()) {
                            session.setAttribute("copy-status", "On Hold");
                            String s = "On Hold";
                            if (book.status.equals("Checked Out")) {
                                s = "/Checked Out";
                                session.setAttribute("due", book.dueDate);
                            }
                            session.setAttribute("hold-patron", book.status.equals("On Hold") ? book.outTo : holdList.get(0).patronBarcode);
                        } else if (book.status.equals("Lost")) {
                            session.setAttribute("copy-status", book.status);
                        } else if (book.note != null) {
                            session.setAttribute("copy-status", book.status);
                            session.setAttribute("note", book.note);
                            if (book.status.equals("Checked Out")) {
                                session.setAttribute("due", book.dueDate);
                            }
                        } else if (book.status.equals("Checked Out")) {
                            session.setAttribute("copy-status", book.status);
                            session.setAttribute("due", book.dueDate);
                        } else {
                            session.setAttribute("copy-status", book.status);
                        }
                        session.setAttribute("status", SC_ACCEPTED);
                        response.sendRedirect("/library/staff.jsp");
                    } else {
                        session.setAttribute("status", SC_BAD_REQUEST);
                        response.sendRedirect("/library/staff.jsp");
                    }
                }
            } else {
                session.setAttribute("status", SC_UNAUTHORIZED);
                response.sendRedirect("/library/dashboard.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
