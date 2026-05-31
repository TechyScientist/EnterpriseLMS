<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Edit Patron Profile"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user != null) {
    if(user.libraryAdmin) {
        int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
        System.out.println(status);
        if(status != SC_OK && status != SC_FOUND && status != SC_ACCEPTED) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
<%          switch(status) {
                case SC_CONFLICT: %>
                    Passwords do not match. Please try again.
<%                  break;
                case SC_BAD_REQUEST: %>
                    That action must be done with the correct form.
<%                  break;
                case SC_NOT_FOUND: %>
                    Patron not found. Please try again.
<%                  break;
                case SC_NOT_ACCEPTABLE: %>
                    Invalid patron barcode. Please try again.
<%                  break;
                case SC_PRECONDITION_FAILED: %>
                    Username already exists. Please try again.
<%                  break;
                case SC_REQUESTED_RANGE_NOT_SATISFIABLE: %>
                    Invalid checkout limit: Patron has already exceeded this limit. Please try again.
<%                  break;
            } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_FOUND) { %>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_ACCEPTED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Patron profile saved successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      }
    if(session.getAttribute("patron") == null) { %>
        <h3>Patron Search</h3>
          <form action="GetPatronInformationServlet" method="post">
                <div class="form-field">
                    <label for="barcode">Patron Barcode</label>
                    <input type="text" name="barcode" id="barcode" required/>
                </div>
              <button type="submit" id="patron-search-submit" name="patron-search-submit">Execute Search<img src="assets/img/proceed.png" alt="Proceed"/></button>
          </form>
    <% } else {
            User patron = (User) session.getAttribute("patron"); %>
            <h3>Edit Patron Profile</h3>
            <form action="EditPatronServlet" method="post">
                <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
                    <div class="form-field" style="margin-bottom: 0;">
                        <label for="barcode">Barcode</label>
                        <input type="text" name="barcode" id="barcode"  required value="<%= patron.barcode %>" readonly/>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
                    <div class="form-field" style="margin-bottom: 0;">
                        <label for="last-name">Last Name</label>
                        <input type="text" name="last-name" id="last-name" required value="<%= patron.lastName %>"/>
                    </div>
                    <div class="form-field" style="margin-bottom: 0;">
                        <label for="first-name">First Name</label>
                        <input type="text" name="first-name" id="first-name" required value="<%= patron.firstName %>"/>
                    </div>
                </div>
                <div class="form-field" style="margin-bottom: 0;">
                    <label for="username">Username</label>
                    <input type="text" name="username" id="username" required value="<%= patron.username %>"/>
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
                    <div class="form-field" style="margin-bottom: 0;">
                        <label for="override-checkout-limit">Checkout Limit</label>
                        <select name="override-checkout-limit" id="override-checkout-limit" required>
                            <option <% if(patron.checkoutLimit == 5) { %> selected <% } %>>Standard (5 items maximum)</option>
                            <option <% if(patron.checkoutLimit != 5) { %> selected <% } %>>Override</option>
                        </select>
                    </div>
                    <div class="form-field" id="limit-div" style="margin-bottom: 0; display: none;">
                        <label for="checkout-limit">Checkout Limit (Items)</label>
                        <input type="number" name="checkout-limit" id="checkout-limit" min="1" step="1" value="<%= patron.checkoutLimit %>" required/>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
                    <div class="form-field" style="margin-bottom: 0;">
                        <label for="override-loan-time">Loan Time</label>
                        <select name="override-loan-time" id="override-loan-time" required>
                            <option <% if(patron.loanTime == 14) { %> selected <% } %>>Standard (14 days)</option>
                            <option <% if(patron.loanTime != 14) { %> selected <% } %>>Override</option>
                        </select>
                    </div>
                    <div class="form-field" id="loan-div" style="margin-bottom: 0; display: none;">
                        <label for="loan-time">Loan Time (Days)</label>
                        <input type="number" name="loan-time" id="loan-time" value="<%= patron.loanTime %>" required/>
                    </div>
                </div>
                <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
                    <div class="form-field">
                        <label for="staff">Staff Privileges</label>
                        <select name="staff" id="staff" required>
                            <option value="0"  <% if(!patron.libraryStaff) { %> selected <% } %>>Disabled</option>
                            <option value="1" <% if(patron.libraryStaff) { %> selected <% } %>>Enabled</option>
                        </select>
                    </div>
                    <div class="form-field">
                        <label for="admin">Administrator Privileges</label>
                        <select name="admin" id="admin" required>
                            <option value="0" <% if(!patron.libraryAdmin) { %> selected <% } %>>Disabled</option>
                            <option value="1" <% if(patron.libraryAdmin) { %> selected <% } %>>Enabled</option>
                        </select>
                    </div>
                </div>
                <button type="submit" id="edit-patron-submit" name="edit-patron-submit">Commit Changes<img src="assets/img/proceed.png" alt="Proceed"/></button>
            </form>

            <script>
                document.addEventListener("DOMContentLoaded", () => {
                    const limitSelector = document.getElementById("override-checkout-limit");
                    const limitDiv = document.getElementById("limit-div");
                    const loanSelector = document.getElementById("override-loan-time");
                    const loanDiv = document.getElementById("loan-div");
                    const passwordField = document.getElementById("password");
                    const confirmField = document.getElementById("confirm-password");

                    limitDiv.style.display = limitSelector.selectedIndex === 0 ? "none" : "block";
                    loanDiv.style.display = loanSelector.selectedIndex === 0 ? "none" : "block";

                    limitSelector.addEventListener("change", (event) => {
                        limitDiv.style.display = event.target.selectedIndex === 0 ? "none" : "block";
                        if(event.target.selectedIndex === 0) {
                            document.getElementById("checkout-limit").value = "5"
                        }
                    });

                    loanSelector.addEventListener("change", (event) => {
                        loanDiv.style.display = event.target.selectedIndex === 0 ? "none" : "block";
                        if(event.target.selectedIndex === 0) {
                            document.getElementById("loan-time").value = "14"
                        }
                    });

                    passwordField.addEventListener("input", () => {
                        confirmField.required = passwordField.value.length > 0;
                    })
                });
            </script>
    <% }
    } else {
        session.setAttribute("status", SC_FORBIDDEN);
        response.sendRedirect("/library/dashboard.jsp");
    }
    session.removeAttribute("status");
    session.removeAttribute("patron"); %>
    <%@ include file="assets/include/footer.jsp" %>
<% } else {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
} %>