<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Create Patron Account"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user != null) {
    if(user.libraryStaff || user.libraryAdmin) { %>
<h3>Create a Patron Account</h3>
  <form action="" method="post">
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
        <div class="form-field" style="margin-bottom: 0;">
            <label for="barcode-type">Barcode Type</label>
            <select id="barcode-type" name="barcode-type">
                <option selected>Generate New Barcode</option>
                <option>Specify Barcode</option>
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
      <button type="submit" id="add-patron-submit" name="add-patron-submit">Create Patron Account<img src="assets/img/proceed.png" alt="Proceed"/></button>
  </form>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const typeSelector = document.getElementById("barcode-type");
        const barcodeDiv = document.getElementById("barcode-div");
        const barcodeField = document.getElementById("barcode");

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
    });
</script>
<%   } else {
    session.setAttribute("status", SC_FORBIDDEN);
    response.sendRedirect("/library/dashboard.jsp");
    }
} else {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
} %>

<%@ include file="assets/include/footer.jsp" %>