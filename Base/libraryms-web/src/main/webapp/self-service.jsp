<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "self-service", pageTitle = "Self Service"; %>
<%@ include file="assets/include/header.jsp" %>
<% if(user == null) {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
}
else {
    int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
    if(status != SC_OK && status != SC_ACCEPTED && status != SC_CONFLICT) { %>
        <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
            <% switch(status) {
                case SC_BAD_REQUEST: %>
                    Invalid copy barcode.
            <%      break;
                case SC_NOT_ACCEPTABLE: %>
                    Missing or empty parameter.
            <%      break;
                case SC_NOT_FOUND:%>
                    Copy not found. Please double check the barcode and try again.
            <%      break;
            } %>
        </p>
        <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
    <%  } else if(status == SC_CONFLICT) { %>
            <p id="note"><img src="assets/img/note.png" alt="Note"/><strong>Note</strong>: Copy already available</p>
            <audio src="assets/sound/chime.mp3" style="display: none;" autoplay></audio>
    <% }
        else {
            if(session.getAttribute("play-sound") != null) {%>
<audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      }
} %>
    <div style="display: grid; grid-template-columns: auto auto; gap: 50px; width: max-content;">
        <div>
            <h3>Check Out a Title</h3>
            <form action="" method="POST">
                <div class="form-field">
                    <label for="patron-barcode">Patron Barcode</label>
                    <input type="text" id="patron-barcode" name="patron-barcode" required disabled value="<%= user.barcode %>"/>
                </div>
                <div class="form-field">
                    <label for="copy-barcode">Copy Barcode</label>
                    <input type="text" id="copy-barcode" name="copy-barcode" required/>
                </div>
                <button type="submit" name="checkout-submit">Check Out <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
        <!-- TODO: Replace this with renewal form -- users should not be able to check in, this should be done by library staff -->
        <div>
            <h3>Check In a Title</h3>
            <form action="CheckInServlet" method="POST">
                <div class="form-field">
                    <label for="copy-barcode">Copy Barcode</label>
                    <input type="text" id="copy-barcode" name="copy-barcode" required/>
                </div>
                <button type="submit" name="checkin-submit">Check In <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
    </div>
    <%@ include file="assets/include/footer.jsp" %>
<%  session.removeAttribute("status");
} %>