<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.*" %>
<% String pageName = "profile", pageTitle = "My Profile"; %>
<%@ include file="assets/include/header.jsp" %>

<%
    if(user == null) {
        session.setAttribute("status", SC_UNAUTHORIZED);
        response.sendRedirect("/library/signin.jsp");
    }
    else {
        int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
        if(status != SC_OK && status != SC_ACCEPTED) {%>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
    <%      switch(status) {
                case SC_CONFLICT: %>
                    Passwords do not match. Please try again.
    <%              break;
                case SC_BAD_REQUEST: %>
                    That action must be done with the correct form.
    <%              break;
                } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_ACCEPTED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Profile saved successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      } %>

    <p id="note"><img src="assets/img/note.png" alt="Note"/><strong>Note</strong>: You cannot change your barcode as this is used to identify you. To change your username, checkout limit, loan time, or staff/admin privileges, see a library administrator.</p>
    <h3 style="display: inline-block;">My Profile</h3>
    <h4 style="margin-left: 30px;">Privileges: <% if(!(user.libraryStaff || user.libraryAdmin)) { %> Standard <% } else { if(user.libraryStaff) { %><span style="display: inline-block; background: var(--color-primary); color: white; padding: 5px; margin-left: 10px; border-radius: 10px;">Staff</span> <% } if(user.libraryAdmin) { %> <span style="display: inline-block; background: var(--color-primary); color: white; padding: 5px; margin-left: 10px; border-radius: 10px;">Administrator</span><% } } %></h4>
    <form action="EditPatronServlet" method="post">
        <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
            <div class="form-field" style="margin-bottom: 0;">
                <label for="barcode">Barcode</label>
                <input type="text" name="barcode" id="barcode"  required value="<%= user.barcode %>" readonly/>
            </div>
        </div>
        <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
            <div class="form-field" style="margin-bottom: 0;">
                <label for="last-name">Last Name</label>
                <input type="text" name="last-name" id="last-name" required value="<%= user.lastName %>"/>
            </div>
            <div class="form-field" style="margin-bottom: 0;">
                <label for="first-name">First Name</label>
                <input type="text" name="first-name" id="first-name" required value="<%= user.firstName %>"/>
            </div>
        </div>
        <div class="form-field" style="margin-bottom: 0;">
            <label for="username">Username</label>
            <input type="text" name="username" id="username" required value="<%= user.username %>" readonly/>
        </div>
        <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
            <div class="form-field" style="margin-bottom: 0;">
                <label for="password">Change Password</label>
                <input type="password" name="password" id="password"/>
            </div>
            <div class="form-field" style="margin-bottom: 0;">
                <label for="confirm-password">Confirm Password</label>
                <input type="password" name="confirm-password" id="confirm-password"/>
            </div>
        </div>
        <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
        <div class="form-field">
            <label for="checkout-limit">Checkout Limit (Items)</label>
            <input type="number" name="checkout-limit" id="checkout-limit" required value="<%= user.checkoutLimit%>" readonly/>
        </div>
            <div class="form-field">
                <label for="loan-time">Loan Time (Days)</label>
                <input type="number" name="loan-time" id="loan-time" value="<%= user.loanTime %>" required/>
            </div>
        </div>
        <input type="hidden" name="staff" id="staff" value="<%= user.libraryStaff ? 1 : 0 %>"/>
        <input type="hidden" name="admin" id="admin" value="<%= user.libraryAdmin ? 1 : 0%>"/>
        <input type="hidden" name="referrer" id="referrer" value="/library/profile.jsp"/>
        <button type="submit" id="edit-patron-submit" name="edit-patron-submit">Commit Changes<img src="assets/img/proceed.png" alt="Proceed"/></button>
    </form>

<% session.removeAttribute("status"); %>

<%@ include file="assets/include/footer.jsp" %>
<% } %>