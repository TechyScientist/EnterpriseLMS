package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.Book;
import com.johnnyconsole.libraryms.persistence.Title;
import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;
import com.johnnyconsole.libraryms.persistence.interfaces.TitleDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("RemoveTitleServlet")
public class RemoveTitleServlet extends HttpServlet {

    @EJB
    private BookDao bookDao;

    @EJB
    private TitleDao titleDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            if(request.getParameter("remove-title-submit") != null) {
                User user = (User) session.getAttribute("user");
                if(user.libraryAdmin) {
                    String barcode = request.getParameter("barcode");
                    if (barcodeBean.isValidTitleBarcode(barcode)) {
                        Title title = titleDao.findByBarcode(barcode);
                        if (title != null) {
                            List<Book> copies = bookDao.findByTitleBarcode(barcode);
                            for(Book book : copies) {
                                if(book.status.equals("Checked Out")) {
                                    session.setAttribute("status", SC_NOT_ACCEPTABLE);
                                    response.sendRedirect("/library/remove-title.jsp");
                                    return;
                                }
                            }
                            titleDao.delete(title);
                            session.setAttribute("status", SC_ACCEPTED);
                            response.sendRedirect("/library/remove-title.jsp");
                        } else {
                            session.setAttribute("status", SC_NOT_FOUND);
                            response.sendRedirect("/library/remove-title.jsp");
                        }
                    } else {
                        session.setAttribute("status", SC_CONFLICT);
                        response.sendRedirect("/library/remove-title.jsp");
                    }
                } else {
                    session.setAttribute("status", SC_FORBIDDEN);
                    response.sendRedirect("/library/dashboard.jsp");
                }
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/remove-title.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
