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

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("AddCopyServlet")
public class AddCopyServlet extends HttpServlet {

    @EJB
    private BarcodeBean barcodeBean;

    @EJB
    private TitleDao titleDao;

    @EJB
    private BookDao bookDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if(user.libraryStaff || user.libraryAdmin) {
                if(request.getParameter("add-copy-submit") != null) {
                    String titleBarcode = request.getParameter("title-barcode"),
                            copyBarcode = request.getParameter("copy-barcode").isEmpty() ?
                                    barcodeBean.generateCopyBarcode() : request.getParameter("copy-barcode");

                    if(barcodeBean.isValidTitleBarcode(titleBarcode)) {
                        if(titleDao.findByBarcode(titleBarcode) != null) {
                            if(barcodeBean.isValidCopyBarcode(copyBarcode)) {
                                if(bookDao.findByCopyCode(copyBarcode) == null) {
                                    bookDao.create(new Book(copyBarcode, titleBarcode));
                                    session.setAttribute("status", SC_CREATED);
                                    response.sendRedirect("/library/add-copy.jsp");
                                } else {
                                    session.setAttribute("status", SC_CONFLICT);
                                    response.sendRedirect("/library/add-copy.jsp");
                                }
                            } else {
                                    session.setAttribute("status", SC_NOT_ACCEPTABLE);
                                    response.sendRedirect("/library/add-copy.jsp");
                            }
                        } else {
                            session.setAttribute("status", SC_NOT_FOUND);
                            response.sendRedirect("/library/add-copy.jsp");
                        }
                    } else {
                        session.setAttribute("status", SC_NOT_ACCEPTABLE);
                        response.sendRedirect("/library/add-copy.jsp");
                    }
                } else {
                    session.setAttribute("status", SC_BAD_REQUEST);
                    response.sendRedirect("/library/add-copy.jsp");
                }
            } else {
                session.setAttribute("status", SC_FORBIDDEN);
                response.sendRedirect("/library/dashboard.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
