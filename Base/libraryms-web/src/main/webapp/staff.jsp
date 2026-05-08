<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Staff Area"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user == null) {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
} else { %>
    <div style="display: grid; grid-template-columns: auto auto; gap: 0 50px; width: max-content;">
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
            <h3>Book Management</h3>
        </div>
    </div>
<%@ include file="assets/include/footer.jsp" %>

<% } %>