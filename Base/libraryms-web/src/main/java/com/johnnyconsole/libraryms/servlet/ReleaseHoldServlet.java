package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.persistence.interfaces.HoldDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("ReleaseHoldServlet")
public class ReleaseHoldServlet extends HttpServlet {

    @EJB
    private HoldDao holdDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            if (request.getParameter("release-submit") != null) {
                String patron = request.getParameter("patron-barcode"),
                        title = request.getParameter("title-barcode");

                if (holdDao.pop(holdDao.retrieve(patron, title))) {
                    session.setAttribute("status", SC_ACCEPTED);
                    response.sendRedirect("/library/dashboard.jsp");
                } else {
                    session.setAttribute("status", SC_CONFLICT);
                    response.sendRedirect("/library/dashboard.jsp");
                }
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
