package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.Hold;
import com.johnnyconsole.libraryms.persistence.interfaces.HoldDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("RequestHoldServlet")
public class RequestHoldServlet extends HttpServlet {

    @EJB
    private HoldDao holdDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") != null) {
            if (request.getParameter("hold-submit") != null) {
                String patron = request.getParameter("patron-barcode"),
                        title = request.getParameter("title-barcode");
                if (title.startsWith("978") && barcodeBean.isValid(title) &&
                        patron.startsWith("13870") && barcodeBean.isValid(patron)) {
                    if (!holdDao.place(new Hold(patron, title))) {
                        session.setAttribute("operation", "hold");
                        session.setAttribute("status", SC_CONFLICT);
                        response.sendRedirect("/library/self-service.jsp");
                    } else {
                        session.setAttribute("status", SC_ACCEPTED);
                        session.setAttribute("operation", "hold");
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
