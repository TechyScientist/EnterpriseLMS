<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.Fine" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<% String pageName = "staff", pageTitle = "Remove a Fine"; %>
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
                    That action must be done with the remove fine form.
<%                  break;
                case SC_NOT_FOUND: %>
                    Patron not found. Please try again.
<%                  break;
            } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
<%      } else if(status == SC_ACCEPTED) { %>
            <p id="success"><img src="assets/img/check.png" alt="Success"/><strong>Success</strong>: Fine removed successfully.</p>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%      }
    if(session.getAttribute("fine-list") == null) { %>
        <h3>Patron Search</h3>
          <form action="GetPatronFineListServlet" method="post">
                <div class="form-field">
                    <label for="barcode">Patron Barcode</label>
                    <input type="text" name="barcode" id="barcode" required/>
                </div>
              <button type="submit" id="fine-list-submit" name="fine-list-submit">Execute Fine Search<img src="assets/img/proceed.png" alt="Proceed"/></button>
          </form>
<% } else {
        @SuppressWarnings("unchecked")
        List<Fine> fines = (List<Fine>) session.getAttribute("fine-list");
        if(fines.isEmpty()) { %>
            <p id="note"><img src="assets/img/note.png" alt="Note"/><strong>Note</strong>: This patron has not been assessed any fines.</p>
            <audio src="assets/sound/chime.mp3" style="display: none;" autoplay></audio>
<%      }
        else { %>
            <h3>Remove a Fine</h3>
            <table>
                <tr>
                    <th>Issued</th>
                    <th>Amount</th>
                    <th>Note</th>
                    <th>Delete</th>
                </tr>
<%            for(Fine fine : fines) { %>
                <tr>
                    <td><%= fine.added.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy h:mm:ss a")) %></td>
                    <td><%= String.format("$%.2f", fine.amount) %></td>
                    <td><%= fine.note %></td>
                    <td>
                        <form action="DeleteFineServlet" method="post" style="margin: 0;">
                            <input type="hidden" name="patron" id="patron" value="<%= fine.patron %>"/>
                            <input type="hidden" name="timestamp" id="timestamp" value="<%= fine.added %>"/>
                            <button type="submit" name="remove-fine-submit" id="remove-fine-submit">Delete<img src="assets/img/proceed.png" alt="Proceed"/></button>
                        </form>
                    </td>
                </tr>
<%            } %>
            </table>
            <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
<%        }
    }
} else {
        session.setAttribute("status", SC_FORBIDDEN);
        response.sendRedirect("/library/dashboard.jsp");
    }
    session.removeAttribute("status");
    session.removeAttribute("fine-list"); %>
    <%@ include file="assets/include/footer.jsp" %>
<% } else {
    session.setAttribute("status", SC_UNAUTHORIZED);
    response.sendRedirect("/library/signin.jsp");
} %>