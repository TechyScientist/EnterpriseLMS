<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<%
    // Metadata strings for dynamic UI elements
    String pageName = "signin",
            pageTitle = "Sign In";
%>
<%@ include file="assets/include/header.jsp" %>

<style>
    .form-field {
        position: relative;
        margin: 20px 0;
    }

    .form-field label {
        position: absolute;
        top: -12px;
        left: 10px;
        padding: 0 5px;
        background: var(--color-background);
    }
</style>

<% int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
if(status != SC_OK) { %>
    <p id="error"><img src="assets/img/cross.png" style="width: 50px; aspect-ratio: 1/1; margin-right: 10px;" alt="Error"/><strong>Error</strong>:
        <% switch(status) {
                case SC_BAD_REQUEST: %>
                    That action must be done by the sign in form.
        <%          break;
                case SC_NOT_ACCEPTABLE: %>
                    Missing or empty parameter.
        <%          break;
                case SC_NOT_FOUND: %>
                    Invalid credentials, please try again.
        <%          break;
                case SC_UNAUTHORIZED: %>
                    You must be signed in to do that.
        <%   } %>
    </p>
    <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<% } %>
<h3>Sign In</h3>
<form action="SignInServlet" method="post">
    <div class="form-field">
        <label for="username">Username</label>
        <input type="text" name="username" id="username" required/>
    </div>
    <div class="form-field">
        <label for="password">Password</label>
        <input type="password" name="password" id="password" required/>
    </div>
    <button type="submit" name="signin-submit">Sign In <img src="assets/img/proceed.png" alt=""/></button>
</form>

<% session.removeAttribute("status"); %>
<%@ include file="assets/include/footer.jsp" %>
