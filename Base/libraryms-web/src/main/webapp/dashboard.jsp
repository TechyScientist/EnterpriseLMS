<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.interfaces.BookDao" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.interfaces.HoldDao" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.Hold" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.interfaces.TitleDao" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.Title" %>
<% String pageName = "dashboard", pageTitle = "Dashboard"; %>
<%@ include file="assets/include/header.jsp" %>

<%
    if(user == null) {
        session.setAttribute("status", SC_UNAUTHORIZED);
        response.sendRedirect("/library/signin.jsp");
    }
    else {
        BookDao bookDao = (BookDao) session.getAttribute("BookDao");
        HoldDao holdDao = (HoldDao) session.getAttribute("HoldDao");
        TitleDao titleDao = (TitleDao) session.getAttribute("TitleDao");
        int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
        if(status != SC_OK) { %>
            <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
                <% switch(status) {
                    case SC_BAD_REQUEST: %>
                That action must be done by the sign in form.
                <%          break;
                    case SC_NOT_ACCEPTABLE: %>
                Missing or empty parameter.
                <%          break;
                    case SC_NOT_FOUND:
                    case SC_UNAUTHORIZED: %>
                Invalid credentials, please try again.
                <%          break;
                } %>
            </p>
            <audio src="assets/sound/bonk.mp3" style="display: none;" autoplay></audio>
    <%  } else {
            if(session.getAttribute("play-sound") != null) {%>
                <audio src="assets/sound/ding.mp3" style="display: none;" autoplay></audio>
    <%      }
        } %>

        <h3 style="display: inline-block;">Signed in as: <%= user.lastName %>, <%= user.firstName %></h3>
        <% if(user.libraryAdmin) {%>
            <p style="display: inline-block; background: var(--color-primary); color: white; padding: 5px; margin-left: 10px; border-radius: 10px;">Library Admin</p>
        <% } else if(user.libraryStaff) {%>
            <p style="display: inline-block; background: var(--color-primary); color: white; padding: 5px; margin-left: 10px; border-radius: 10px;">Library Staff</p>
        <% } %>

        <h3>My Library Account</h3>
        <% List<Book> books = bookDao.checkedOutBy(user.barcode);
            List<Hold> holds = holdDao.listByPatronBarcode(user.barcode); %>
        <p style="margin-left: 10px;"><strong>My Patron Barcode</strong>: <%= user.barcode %></p>
        <p style="margin-left: 10px;"><strong>Account Balance</strong>: <%= String.format("$%.2f", user.balance) %></p>
        <p style="margin-left: 10px;"><strong>Checked Out Materials</strong>: <%= books.size() %></p>
        <% if(!books.isEmpty()) { %>
            <table style="margin: 10px;">
                <tr>
                    <th>Copy Barcode</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Due</th>
                </tr>

                <% for(Book book : books) {
                    //TODO: FIX - does not work if the user has a checked out book
                    Title titleInfo = titleDao.findByBarcode(book.titleBarcode); %>
                    <tr>
                        <th><%= book.copyBarcode %></th>
                        <td><%= titleInfo.title %></td>
                        <td><%= titleInfo.author.replace("\n", "<br/>") %></td>
                        <td><% if(book.dueDate.before(Date.valueOf(LocalDate.now()))) { %><strong>Overdue</strong><br/><% } %><%= book.dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) %></td>
                    </tr>
                <% } %>
            </table>
        <% } %>
        <p style="margin: 10px;"><strong>Holds</strong>: <%= holds.size() %></p>
        <% if(!holds.isEmpty()) { %>
            <table style="margin: 10px;">
                <tr>
                    <th>Hold For</th>
                    <th>Placed</th>
                </tr>

                <% for(Hold hold : holds) { %>
                    <tr>
                        <td><strong><%= hold.titleBarcode %></strong> (<%= titleDao.findByBarcode(hold.titleBarcode).title %>)</td>
                        <td><%= hold.placed.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMMM yyyy h:mm:ss a")) %></td>
                    </tr>
                <% } %>
            </table>
        <% } %>
        <p style="margin: 10px;">Visit the <a href="self-service.jsp">Self Service</a> page to check out materials, renew checked out materials, or place holds.</p>
<% session.removeAttribute("status");
session.removeAttribute("play-sound");
} %>
<%@ include file="assets/include/footer.jsp" %>