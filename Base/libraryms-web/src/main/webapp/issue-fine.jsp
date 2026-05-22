<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Assess a Fine"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user != null) {
    if(user.libraryStaff || user.libraryAdmin) {
        int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
        if(status != SC_OK && status != SC_CREATED) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
<%          switch(status) {
                case SC_NOT_ACCEPTABLE: %>
                    Invalid barcode. Please try again.
<%                  break;
                case SC_BAD_REQUEST: %>
                    That action must be done with the assess fine form.
<%                  break;
                case SC_NOT_FOUND: %>
                    Patron not found. Please try again.
<%                  break;
            } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_CREATED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Fine Assessed successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      } %>
<h3>Delete a Patron Profile</h3>
  <form action="" method="post">
      <div class="form-field">
          <label for="barcode">Patron Barcode</label>
          <input type="text" name="barcode" id="barcode" required/>
      </div>
      <div class="form-field">
          <label for="amount">Fine Amount</label>
          <input type="number" name="amount" id="amount" required/>
      </div>
      <div class="form-field">
          <label for="note">Note</label>
          <textarea name="note" id="note" required></textarea>
      </div>
      <button type="submit" id="assess-fine-submit" name="assess-fine-submit">Assess Fine<img src="assets/img/proceed.png" alt="Proceed"/></button>
  </form>

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