<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "staff", pageTitle = "Delete Patron Profile"; %>
<%@ include file="assets/include/header.jsp" %>

<% if(user != null) {
    if(user.libraryStaff || user.libraryAdmin) {
        int status = session.getAttribute("status") == null ? SC_OK : (int) session.getAttribute("status");
        if(status != SC_OK && status != SC_ACCEPTED) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
<%          switch(status) {
                case SC_CONFLICT: %>
                    Invalid barcode. Please try again.
<%                  break;
                case SC_NOT_ACCEPTABLE: %>
                    Unable to delete patron profile - patron has checked out books.
<%                  break;
                case SC_BAD_REQUEST: %>
                    That action must be done with the delete patron form.
<%                  break;
                case SC_NOT_FOUND: %>
                    Patron not found. Please try again.
<%                  break;
            } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_ACCEPTED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Patron profile deleted successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      } %>
<h3>Delete a Patron Profile</h3>
  <form action="RemovePatronServlet" method="post">
        <div class="form-field">
            <label for="barcode">Patron Barcode</label>
            <input type="text" name="barcode" id="barcode" required/>
        </div>
      <button type="submit" id="remove-patron-submit" name="remove-patron-submit">Delete Patron Profile<img src="assets/img/proceed.png" alt="Proceed"/></button>
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