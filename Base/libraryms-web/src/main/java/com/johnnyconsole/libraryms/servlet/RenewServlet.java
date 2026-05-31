package com.johnnyconsole.libraryms.servlet;

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

@WebServlet("RenewServlet")
public class RenewServlet extends HttpServlet {

    @EJB
    private BookDao bookDao;

    @EJB
    private UserDao userDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            if (request.getParameter("renew-submit") != null) {
                String copy = request.getParameter("copy-barcode");
                User patron = userDao.findByBarcode(request.getParameter("patron-barcode"));
                bookDao.renew(bookDao.findByCopyCode(copy), patron.loanTime);
                session.setAttribute("status", SC_NO_CONTENT);
                response.sendRedirect("/library/dashboard.jsp");
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/dashboard.jsp");
            }
        }
        else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
