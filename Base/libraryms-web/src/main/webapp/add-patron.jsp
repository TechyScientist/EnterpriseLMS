<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Create Patron Profile"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user != null) {
    if(user.libraryStaff || user.libraryAdmin) {
        int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
        if(status != SC_OK && status != SC_CREATED) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
<%          switch(status) {
                case SC_CONFLICT: %>
                    Passwords do not match. Please try again.
<%                  break;
                case SC_NOT_ACCEPTABLE: %>
                    Invalid barcode, or Username or barcode already exists. Please try again.
<%                  break;
                case SC_BAD_REQUEST: %>
                    That action must be done with the add patron form.
<%                  break;
            } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_CREATED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Patron profile created successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      } %>
<h3>Create a Patron Profile</h3>
  <form action="AddPatronServlet" method="post">
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
        <div class="form-field" style="margin-bottom: 0;">
            <label for="barcode-type">Barcode Type</label>
            <select id="barcode-type" name="barcode-type">
                <option selected>Generated</option>
                <option>Specified</option>
            </select>
        </div>
        <div class="form-field" id="barcode-div" style="display: none; margin-bottom: 0;">
            <label for="barcode">Barcode</label>
            <input type="text" name="barcode" id="barcode"/>
        </div>
      </div>
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
          <div class="form-field" style="margin-bottom: 0;">
              <label for="last-name">Last Name</label>
              <input type="text" name="last-name" id="last-name" required/>
          </div>
          <div class="form-field" style="margin-bottom: 0;">
              <label for="first-name">First Name</label>
              <input type="text" name="first-name" id="first-name" required/>
          </div>
      </div>
      <div class="form-field" style="margin-bottom: 0;">
          <label for="username">Username</label>
          <input type="text" name="username" id="username" required/>
      </div>
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
          <div class="form-field" style="margin-bottom: 0;">
              <label for="password">Password</label>
              <input type="password" name="password" id="password" required/>
          </div>
          <div class="form-field" style="margin-bottom: 0;">
              <label for="confirm-password">Confirm Password</label>
              <input type="password" name="confirm-password" id="confirm-password" required/>
          </div>
      </div>
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
          <div class="form-field" style="margin-bottom: 0;">
              <label for="override-checkout-limit">Checkout Limit</label>
              <select name="override-checkout-limit" id="override-checkout-limit" required>
                  <option selected>Standard (5 items maximum)</option>
                  <option>Override</option>
              </select>
          </div>
          <div class="form-field" id="limit-div" style="margin-bottom: 0; display: none;">
              <label for="checkout-limit">Checkout Limit</label>
              <input type="number" name="checkout-limit" id="checkout-limit" min="1" step="1" value="5" required/>
          </div>
      </div>
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
          <div class="form-field" style="margin-bottom: 0;">
              <label for="override-loan-time">Loan Tame</label>
              <select name="override-loan-time" id="override-loan-time" required>
                  <option selected>Standard (14 days)</option>
                  <option>Override</option>
              </select>
          </div>
          <div class="form-field" id="loan-div" style="margin-bottom: 0; display: none;">
              <label for="loan-time">Loan Time</label>
              <input type="number" name="loan-time" id="loan-time" min="1" step="1" value="14" required/>
          </div>
      </div>
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
          <div class="form-field">
              <label for="staff">Staff Privileges</label>
              <select name="staff" id="staff" required>
                  <option value="0" selected>Disabled</option>
                  <option value="1" <% if(!user.libraryAdmin) { %> disabled <% } %>>Enabled</option>
              </select>
          </div>
          <div class="form-field">
              <label for="admin">Administrator Privileges</label>
              <select name="admin" id="admin" required>
                  <option value="0" selected>Disabled</option>
                  <option value="1" <% if(!user.libraryAdmin) { %> disabled <% } %>>Enabled</option>
              </select>
          </div>
      </div>
      <button type="submit" id="add-patron-submit" name="add-patron-submit">Create Patron Profile<img src="assets/img/proceed.png" alt="Proceed"/></button>
  </form>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const typeSelector = document.getElementById("barcode-type");
        const barcodeDiv = document.getElementById("barcode-div");
        const barcodeField = document.getElementById("barcode");
        const limitSelector = document.getElementById("override-checkout-limit");
        const limitDiv = document.getElementById("limit-div");
        const loanSelector = document.getElementById("override-loan-time");
        const loanDiv = document.getElementById("loan-div");

        typeSelector.addEventListener("change", (event) => {
            if(event.target.selectedIndex === 0) {
                barcodeDiv.style.display = "none";
                barcodeField.required = false;
            }
            else {
                barcodeDiv.style.display = "block";
                barcodeField.required = true;
            }
        });

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
    });
</script>
<% } else {
        session.setAttribute("status", SC_FORBIDDEN);
        response.sendRedirect("/library/dashboard.jsp");
    }
    session.removeAttribute("status"); %>
    <%@ include file="assets/include/footer.jsp" %>
<% } else {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
} %>