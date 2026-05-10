<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<% String pageName = "staff", pageTitle = "Staff Area"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user == null) {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
} else if(!(user.libraryStaff || user.libraryAdmin)) {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/dashboard.jsp");
} else {
    int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
    String operation = (String) session.getAttribute("operation");
    if(status != SC_OK && status != SC_CONTINUE && status != SC_PARTIAL_CONTENT && status != SC_ACCEPTED) {
        switch(status) {
            case SC_CONFLICT:
                if(operation.equals("checkout")) {
                    //TODO: Implement this error handler
                }
                if(operation.equals("checkin")) { %>
                    <p id="note"><img src="assets/img/note.png" alt="Note"/><strong>Note</strong>: Copy already available.</p>
                    <audio src="assets/sound/chime.mp3" style="display: none;" autoplay></audio>
        <%      }
                break;
            case SC_NOT_FOUND: %>
                <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>: Patron or copy not found. Please try again.</p>
                <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
      <%        break;
            case SC_BAD_REQUEST: %>
                <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>: Invalid patron or copy barcode. Please try again.</p>
                <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
      <%    }
    }
    else if(status == SC_ACCEPTED) {
        if(operation.equals("checkin")) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Check in completed -&nbsp;<strong>On Time</strong></p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
    <%  }
    }
    else if(status == SC_CONTINUE) {
        if(operation.equals("checkin")) { %>
            <p id="note"><img src="assets/img/note.png" alt="Note"/><strong>Success</strong>: Check in completed - This title is &nbsp;<strong>On hold </strong>&nbsp;by patron&nbsp;<strong><%= (String) session.getAttribute("patron") %></strong>.</p>
            <audio src="assets/sound/twinkle.mp3" style="display: none;" autoplay></audio>
     <% }
    }
    else if(status == SC_PARTIAL_CONTENT) {
        if(operation.equals("checkin")) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Success</strong>: Check in completed&nbsp;-&nbsp;<strong>Overdue</strong>&nbsp;(due <%= ((Date) session.getAttribute("due")).toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) %>)</p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
     <% }
    } %>
    <div style="display: grid; grid-template-columns: auto auto auto; gap: 0 50px; width: max-content;">
        <div>
            <h3>Quick Action: Check Copy Status</h3>
            <form action="" method="POST">
                <div class="form-field">
                    <label for="copy-barcode">Copy Barcode</label>
                    <input type="text" id="copy-barcode" name="copy-barcode" required/>
                </div>
                <button type="submit" name="status-check-submit">Check Status <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
        <div>
            <h3>Quick Action: Check Out a Title</h3>
            <form action="CheckOutServlet" method="POST">
                <input type="hidden" name="referrer" id="referrer" value="/library/staff.jsp" />
                <div class="form-field">
                    <label for="patron-barcode">Patron Barcode</label>
                    <input type="text" id="patron-barcode" name="patron-barcode" required/>
                </div>
                <div class="form-field">
                    <label for="copy-barcode">Copy Barcode</label>
                    <input type="text" id="copy-barcode" name="copy-barcode" required/>
                </div>
                <button type="submit" name="checkout-submit">Check Out <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
        <div>
            <h3>Quick Action: Check In a Title</h3>
            <form action="CheckInServlet" method="POST">
                <div class="form-field">
                    <label for="copy-barcode">Copy Barcode</label>
                    <input type="text" id="copy-barcode" name="copy-barcode" required/>
                </div>
                <button type="submit" name="checkin-submit">Check In <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
        <div>
            <h3>Patron Management</h3>
        </div>
        <div>
            <h3>Collection Management</h3>
        </div>
    </div>
<%@ include file="assets/include/footer.jsp" %>
<% session.removeAttribute("status");
session.removeAttribute("operation");
session.removeAttribute("patron");
session.removeAttribute("due");
} %>
