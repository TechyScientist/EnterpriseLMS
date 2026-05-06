<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "dashboard", pageTitle = "Dashboard"; %>
<%@ include file="assets/include/header.jsp" %>

<%
    if(user == null) {
        session.setAttribute("status", SC_UNAUTHORIZED);
        response.sendRedirect("/libraryms/signin.jsp");
    }
    else {
        int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
        if(status != SC_OK) { %>
            <p id="error"><strong>Error</strong>:
                <% switch(status) {
                    case SC_BAD_REQUEST: %>
                That action must be done by the sign in form.
                <%          break;
                    case SC_NOT_ACCEPTABLE: %>
                Missing or empty parameter.
                <%          break;
                    case SC_NOT_FOUND:
                    case SC_UNAUTHORIZED: %>
                Invalid credentials, please try again.
                <%          break;
                } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
    <%  } else {
            if(session.getAttribute("play-sound") != null) {%>
                <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
    <%      }
        } %>

        <h3 style="display: inline-block;">Signed in as: <%= user.lastName %>, <%= user.firstName %></h3>
        <% if(user.libraryAdmin) {%>
            <p style="display: inline-block; background: var(--color-primary); color: white; padding: 5px; margin-left: 10px; border-radius: 10px;">Library Admin</p>
        <% } else if(user.libraryStaff) {%>
            <p style="display: inline-block; background: var(--color-primary); color: white; padding: 5px; margin-left: 10px; border-radius: 10px;">Library Staff</p>
        <% } %>

<% session.removeAttribute("status");
session.removeAttribute("play-sound");
} %>
<%@ include file="assets/include/footer.jsp" %>