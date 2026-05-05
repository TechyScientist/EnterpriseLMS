<% String pageName = "home", pageTitle = null; %>
<%@ include file="assets/include/header.jsp" %>
<% if(session.getAttribute("signed-out") != null) { %>
    <p id="success"><img src="assets/img/check.png" style="width: 50px; aspect-ratio: 1/1; margin-right: 10px;" alt="Success"/> <strong>Success</strong>: You have been signed out. See you next time!</p>
    <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
    <% session.removeAttribute("signed-out"); %>
<% } %>
<%@ include file="assets/include/footer.jsp" %>