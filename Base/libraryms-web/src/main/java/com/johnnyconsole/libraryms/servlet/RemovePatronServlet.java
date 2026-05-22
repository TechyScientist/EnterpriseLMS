package com.johnnyconsole.libraryms.servlet;

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

@WebServlet("RemovePatronServlet")
public class RemovePatronServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    @EJB
    private BookDao bookDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            if(request.getParameter("remove-patron-submit") != null) {
                User user = (User) session.getAttribute("user");
                if(user.libraryAdmin) {
                    String barcode = request.getParameter("barcode");
                    if (barcodeBean.isValidPatronBarcode(barcode)) {
                        User patron = userDao.findByBarcode(barcode);
                        if (patron != null) {
                            if (bookDao.checkedOutBy(patron.barcode).isEmpty()) {
                                userDao.delete(patron);
                                session.setAttribute("status", SC_ACCEPTED);
                                response.sendRedirect("/library/remove-patron.jsp");
                            } else {
                                session.setAttribute("status", SC_NOT_ACCEPTABLE);
                                response.sendRedirect("/library/remove-patron.jsp");
                            }
                        } else {
                            session.setAttribute("status", SC_NOT_FOUND);
                            response.sendRedirect("/library/remove-patron.jsp");
                        }
                    } else {
                        session.setAttribute("status", SC_CONFLICT);
                        response.sendRedirect("/library/remove-patron.jsp");
                    }
                } else {
                    session.setAttribute("status", SC_FORBIDDEN);
                    response.sendRedirect("/library/dashboard.jsp");
                }
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/remove-patron.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
