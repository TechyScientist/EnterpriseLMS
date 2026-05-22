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
import java.sql.Timestamp;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("DeleteFineServlet")
public class DeleteFineServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    @EJB
    private FineDao fineDao;

    @EJB
    private BarcodeBean barcodeBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") != null) {
            if(request.getParameter("remove-fine-submit") != null) {
                User user = (User) session.getAttribute("user");
                if(user.libraryStaff || user.libraryAdmin) {
                    String patron = request.getParameter("patron");
                    Timestamp timestamp = Timestamp.valueOf(request.getParameter("timestamp"));
                    fineDao.remove(fineDao.search(patron, timestamp));
                    session.setAttribute("status", SC_ACCEPTED);
                    response.sendRedirect("/library/remove-fine.jsp");
                }  else {
                    session.setAttribute("status", SC_FORBIDDEN);
                    response.sendRedirect("/library/dashboard.jsp");
                }
            } else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/remove-fine.jsp");
            }
        } else {
            session.setAttribute("status", SC_UNAUTHORIZED);
            response.sendRedirect("/library/signin.jsp");
        }
    }
}
