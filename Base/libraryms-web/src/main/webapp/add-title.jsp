<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Add Title Record"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user != null) {
    if(user.libraryStaff || user.libraryAdmin) {
        int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
        if(status != SC_OK && status != SC_CREATED) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
<%          switch(status) {
                case SC_CONFLICT: %>
                    Title record already exists. Perhaps you meant to <a href="/library/add-copy.jsp">Add a Title</a> instead.
<%                  break;
                case SC_NOT_ACCEPTABLE: %>
                    Invalid barcode. Please try again.
<%                  break;
                case SC_BAD_REQUEST: %>
                    That action must be done with the add title form.
<%                  break;
            } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_CREATED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Title record created successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      } %>
<h3>Create a Title Record</h3>
  <form action="" method="post">
    <div class="form-field" style="margin-bottom: 0;">
        <label for="barcode">Barcode</label>
        <input type="text" name="barcode" id="barcode" required/>
    </div>
      <div class="form-field">
          <label for="title">Title Name</label>
          <input type="text" name="title" id="title" required/>
      </div>
      <div class="form-field">
          <label for="authors">Author(s)</label>
          <textarea name="authors" id="authors" required></textarea>
      </div>
      <div class="form-field">
          <label for="add-copy">Add Initial Copy?</label>
          <select id="add-copy" name="add-copy" required>
              <option selected>No</option>
              <option>Yes</option>
          </select>
      </div>
      <div style="display: grid; grid-template-columns: min-content min-content; gap: 10px;">
          <div class="form-field" id="type-div" style="display: none; margin-top: 0;">
              <label for="barcode-type">Barcode Type</label>
              <select id="barcode-type" name="barcode-type">
                  <option selected>Generated</option>
                  <option>Specified</option>
              </select>
          </div>
          <div class="form-field" id="barcode-div" style="display: none; margin-top: 0;">
              <label for="copy-barcode">Copy Barcode</label>
              <input type="text" name="copy-barcode" id="copy-barcode" required/>
          </div>
      </div>
      <button type="submit" id="add-title-submit" name="add-title-submit">Add Title Record<img src="assets/img/proceed.png" alt="Proceed"/></button>
  </form>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const initialCopy = document.getElementById("add-copy");
        const typeDiv = document.getElementById("type-div");
        const barcodeDiv = document.getElementById("barcode-div");
        const typeSelector = document.getElementById("barcode-type");
        const barcodeField = document.getElementById("copy-barcode");

        initialCopy.addEventListener("change", (event) => {
            typeDiv.style.display = event.target.selectedIndex === 1 ? "block" : "none";
            typeSelector.required = event.target.selectedIndex === 1
            if(event.target.selectedIndex === 0) {
                barcodeDiv.style.display = "none"
                barcodeField.required = false;
            }
        });

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