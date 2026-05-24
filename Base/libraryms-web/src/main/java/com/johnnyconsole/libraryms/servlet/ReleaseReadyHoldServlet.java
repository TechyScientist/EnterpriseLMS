package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("ReleaseReadyHoldServlet")
public class ReleaseReadyHoldServlet extends HttpServlet {

    @EJB
    private BookDao bookDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            if (request.getParameter("release-submit") != null) {
                String copy = request.getParameter("copy-barcode");
                bookDao.checkIn(bookDao.findByCopyCode(copy));
                session.setAttribute("status", SC_ACCEPTED);
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
