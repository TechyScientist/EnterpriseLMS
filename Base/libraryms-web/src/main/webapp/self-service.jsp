<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "self-service", pageTitle = "Self Service"; %>
<%@ include file="assets/include/header.jsp" %>
<% if(user == null) {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
}
else {
    int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
    String operation = (String) session.getAttribute("operation");
    if(status != SC_OK && status != SC_ACCEPTED && status != SC_CONFLICT) { %>
        <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
            <% switch(status) {
                case SC_BAD_REQUEST: %>
                    Invalid barcode. Please try again.
            <%      break;
                case SC_NOT_ACCEPTABLE: %>
                    <%= operation.equals("checkout") ? "You must see a library staff member to check this book." : "Missing or empty parameter." %>
            <%      break;
                case SC_NOT_FOUND:%>
                    <%= operation.equals("hold") ? "Title" : "Copy" %> not found. Please double check the barcode and try again.
            <%      break;
            } %>
        </p>
        <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
    <%  } else if(status == SC_CONFLICT) {
            if(operation.equals("checkout")) { %>
                <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>: Copy is either checked out or lost.</p>
                <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
    <%          } else if(operation.equals("hold")) { %>
                <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>: You already have a hold on that title.</p>
                <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
             <% }
        } else if(status == SC_ACCEPTED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><%= operation.equals("hold") ? "Hold" : "Check Out" %> successful. <% if(operation.equals("checkout")) { %> Your book is due&nbsp;<strong><%= (String) session.getAttribute("due-date") %></strong>.<% } %></p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
     <% }%>
    <div style="display: grid; grid-template-columns: auto auto; gap: 50px; width: max-content;">
        <div>
            <h3>Check Out a Title</h3>
            <form action="CheckOutServlet" method="POST">
                <input type="hidden" name="referrer" id="referrer" value="/library/self-service.jsp" />
                <input type="hidden" id="patron-barcode" name="patron-barcode" value="<%= user.barcode %>"/>
                <div class="form-field">
                    <label for="copy-barcode">Copy Barcode</label>
                    <input type="text" id="copy-barcode" name="copy-barcode" required/>
                </div>
                <button type="submit" name="checkout-submit">Check Out <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
        <div>
            <h3>Request a Hold</h3>
            <form action="RequestHoldServlet" method="POST">
                <input type="hidden" name="patron-barcode" id="patron-barcode" value="<%= user.barcode %>"/>
                <div class="form-field">
                    <label for="title-barcode">Title Barcode</label>
                    <input type="text" id="title-barcode" name="title-barcode" required/>
                </div>
                <button type="submit" name="hold-submit">Submit Hold Request <img src="assets/img/proceed.png" alt=""></button>
            </form>
        </div>
    </div>

    <%@ include file="assets/include/footer.jsp" %>
<%  session.removeAttribute("status");
    session.removeAttribute("operation");
    session.removeAttribute("due-date");
} %>