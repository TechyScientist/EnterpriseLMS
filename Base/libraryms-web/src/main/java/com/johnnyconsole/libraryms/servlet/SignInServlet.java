package com.johnnyconsole.libraryms.servlet;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.johnnyconsole.libraryms.persistence.User;
import com.johnnyconsole.libraryms.persistence.interfaces.BookDao;
import com.johnnyconsole.libraryms.persistence.interfaces.HoldDao;
import com.johnnyconsole.libraryms.persistence.interfaces.TitleDao;
import com.johnnyconsole.libraryms.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("SignInServlet")
public class SignInServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    @EJB
    private BookDao bookDao;

    @EJB
    private TitleDao titleDao;

    @EJB
    private HoldDao holdDao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("signin-submit") != null) {
            String username = request.getParameter("username"),
                    password = request.getParameter("password");

            if (!(username.isEmpty() && password.isEmpty())) {
                User user;
                if(username.startsWith("1")) {
                    user = userDao.findByBarcode(username);
                }
                else {
                    user = userDao.findByUsername(username);
                }

                if (user != null) {
                    if (BCrypt.verifyer(BCrypt.Version.VERSION_2A)
                            .verifyStrict(password.toCharArray(), user.getPassword().toCharArray())
                            .verified) {
                        session.setAttribute("user", user);
                        session.setAttribute("play-sound", "");
                        session.setAttribute("UserDao", userDao);
                        session.setAttribute("TitleDao", titleDao);
                        session.setAttribute("BookDao", bookDao);
                        session.setAttribute("HoldDao", holdDao);
                        response.sendRedirect("/library/dashboard.jsp");
                    }
                    else {
                        session.setAttribute("status", SC_NOT_ACCEPTABLE);
                        response.sendRedirect("/library/signin.jsp");
                    }
                }
                else {
                    session.setAttribute("status", SC_NOT_FOUND);
                    response.sendRedirect("/library/signin.jsp");
                }
            }
            else {
                session.setAttribute("status", SC_BAD_REQUEST);
                response.sendRedirect("/library/signin.jsp");
            }
        }
    }
}
