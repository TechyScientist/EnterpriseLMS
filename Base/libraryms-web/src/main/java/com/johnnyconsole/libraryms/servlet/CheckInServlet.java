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
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("CheckInServlet")
public class CheckInServlet extends HttpServlet {

    @EJB
    private BookDao bookDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            if (request.getParameter("checkin-submit") != null) {
                String barcode = request.getParameter("copy-barcode");
                if (barcode.charAt(0) >= '2' && barcode.charAt(0) <= '8' && barcodeBean.isValid(barcode)) {
                    Book book = bookDao.findByCopyCode(barcode);
                    if (book == null) {
                        session.setAttribute("status", SC_NOT_FOUND);
                        response.sendRedirect("/library/self-service.jsp");
                    } else if (!book.status.equals("Available")) {
                        //TODO: calculate if the book is late, on time or has a note -- need 3 different status codes
                        bookDao.checkIn(book);
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("operation", "checkin");
                        response.sendRedirect("/library/self-service.jsp");
                    } else {
                        session.setAttribute("status", SC_CONFLICT);
                        session.setAttribute("operation", "checkin");
                        response.sendRedirect("/library/self-service.jsp");
                    }

                } else {
                    session.setAttribute("status", SC_BAD_REQUEST);
                    response.sendRedirect("/library/self-service.jsp");
                }
            }
        }
        else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
