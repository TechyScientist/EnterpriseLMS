package com.johnnyconsole.libraryms.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebServlet("SignInServlet")
public class SignInServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("signin-submit") != null) {
            String username = request.getParameter("username"),
                    password = request.getParameter("password");
                //request.getSession().setAttribute("status", SC_UNAUTHORIZED);
                request.getSession().setAttribute("play-sound", "");
                response.sendRedirect("/libraryms/dashboard.jsp");
        }
    }

}
