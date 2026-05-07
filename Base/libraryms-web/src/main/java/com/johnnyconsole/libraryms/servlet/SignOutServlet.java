package com.johnnyconsole.libraryms.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("SignOutServlet")
public class SignOutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        session.removeAttribute("UserDao");
        session.removeAttribute("BookDao");
        session.removeAttribute("TitleDao");
        session.removeAttribute("HoldDao");
        session.setAttribute("signed-out", "");
        response.sendRedirect("/library/");
    }
}
