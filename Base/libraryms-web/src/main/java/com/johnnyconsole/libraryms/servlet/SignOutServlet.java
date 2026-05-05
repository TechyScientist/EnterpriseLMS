package com.johnnyconsole.libraryms.servlet;

import com.johnnyconsole.libraryms.persistence.interfaces.UserDao;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("SignOutServlet")
public class SignOutServlet extends HttpServlet {

    @EJB
    private UserDao userDao;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.setAttribute("signed-out", "");
        response.sendRedirect("/libraryms/");
    }
}
