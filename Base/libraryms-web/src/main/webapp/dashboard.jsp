<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "dashboard", pageTitle = "Dashboard"; %>

<script>
    document.onDOMContentLoaded = function() {
        document.getElementsByTagName("iframe")[0].allow = "autoplay";
    }
</script>

<%@ include file="assets/include/header.jsp" %>
<% int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
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

<%@ include file="assets/include/footer.jsp" %>
<% session.removeAttribute("status");
session.removeAttribute("play-sound"); %>