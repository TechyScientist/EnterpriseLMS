package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.interfaces.BarcodeBean;
import com.johnnyconsole.libraryms.persistence.Fine;
import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.FineDao;
import com.johnnyconsole.libraryms.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("AssessFineServlet")
public class AssessFineServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    @EJB
    private FineDao fineDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            if(request.getParameter("assess-fine-submit") != null) {
                User user = (User) session.getAttribute("user");
                if(user.libraryStaff || user.libraryAdmin) {
                    String barcode = request.getParameter("barcode"),
                        note = request.getParameter("note");
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    if (barcodeBean.isValidPatronBarcode(barcode)) {
                        User patron = userDao.findByBarcode(barcode);
                        if (patron != null) {
                                fineDao.assess(new Fine(patron.barcode, amount, note));
                                session.setAttribute("status", SC_CREATED);
                                response.sendRedirect("/library/issue-fine.jsp");
                            } else {
                                session.setAttribute("status", SC_NOT_FOUND);
                                response.sendRedirect("/library/issue-fine.jsp");
                            }
                        } else {
                            session.setAttribute("status", SC_NOT_ACCEPTABLE);
                            response.sendRedirect("/library/issue-fine.jsp");
                        }
                    }
                else {
                    session.setAttribute("status", SC_FORBIDDEN);
                    response.sendRedirect("/library/dashboard.jsp");
                }
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/issue-fine.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
