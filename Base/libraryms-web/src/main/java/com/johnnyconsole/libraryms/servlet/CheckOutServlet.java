package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.Book;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;

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
    private BookDao bookDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            if (request.getParameter("checkout-submit") != null) {
                String patron = request.getParameter("patron-barcode"),
                        copy = request.getParameter("copy-barcode"),
                        referrer = request.getParameter("referrer");
                if (copy.charAt(0) >= '2' && copy.charAt(0) <= '8' && barcodeBean.isValid(copy) &&
                    patron.startsWith("13870") && barcodeBean.isValid(patron)) {
                    Book book = bookDao.findByCopyCode(copy);
                    if (book == null) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect(referrer);
                    } else if (!book.status.equals("Available")) {
                        session.setAttribute("status", SC_CONFLICT);
                        session.setAttribute("operation", "checkout");
                        response.sendRedirect(referrer);
                    } else {
                        bookDao.checkOut(book, patron);
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("operation", "checkout");
                        session.setAttribute("due-date", book.dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                        response.sendRedirect(referrer);
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
