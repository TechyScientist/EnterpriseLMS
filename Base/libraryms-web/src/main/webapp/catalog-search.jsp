<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<%@ page import="com.johnnyconsole.libraryms.persistence.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<% String pageName = "catalog-search", pageTitle = "Catalog Search"; %>
<%@ include file="assets/include/header.jsp" %>
<% int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
   String criteria = session.getAttribute("criteria") == null ? "copyBarcode" : (String) session.getAttribute("criteria");
    if(status != SC_OK && status != SC_ACCEPTED) { %>
        <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>:
            <% switch(status) {
                case SC_NOT_FOUND: %>
                    No copies found. Please double-check your search criteria and try again.
            <%      break;
                case SC_NOT_ACCEPTABLE: %>
                    Invalid copy or title barcode. Please try again.
            <%      break;
            } %>

            </p>
            <audio src="assets/sound/bonk.mp3" style="display:none;" autoplay></audio>
    <% } %>
        <h3>Catalog Search</h3>
        <form action="CatalogSearchServlet" method="POST">
            <div class="form-field">
                <label for="searchBy">Search By</label>
                <select name="searchBy" id="searchBy">
                    <option value="copyBarcode" <% if(criteria.equals("copyBarcode")) { %> selected <% } %>>Copy Barcode (Exact Match)</option>
                    <option value="titleBarcode" <% if(criteria.equals("titleBarcode")) { %> selected <% } %>>Title Barcode (Exact Match)</option>
                    <option value="title" <% if(criteria.equals("title")) { %> selected <% } %>>Title (Contains)</option>
                    <option value="author" <% if(criteria.equals("author")) { %> selected <% } %>>Author Name (Contains)</option>
                </select>
            </div>
            <div class="form-field" id="copyBarcodeSearch" <% if(!criteria.equals("copyBarcode")) { %> style="display: none;" <% } %>>
                <label for="copyBarcode">Copy Barcode</label>
                <input type="text" name="copyBarcode" id="copyBarcode" <% if(criteria.equals("copyBarcode")) { %> required <% } %>/>
            </div>
            <div class="form-field" id="titleBarcodeSearch" <% if(!criteria.equals("titleBarcode")) { %> style="display: none;" <% } %>>
                <label for="titleBarcode">Title Barcode</label>
                <input type="text" name="titleBarcode" id="titleBarcode" <% if(criteria.equals("titleBarcode")) { %> required <% } %>/>
            </div>
            <div class="form-field" id="criteriaSearch" <% if(criteria.contains("Barcode")) { %> style="display: none;" <% } %>>
                <label for="criteria">Search Criteria</label>
                <input type="text" name="criteria" id="criteria" <% if(!criteria.contains("Barcode")) { %> required <% } %>/>
            </div>
            <button type="submit" id="search-submit" name="search-submit">Search <img src="assets/img/proceed.png" alt="Search"/></button>
        </form>
<% if(status == SC_ACCEPTED) { %>
    <h3>Search Result</h3>
    <% if(session.getAttribute("book") != null) {
        Book book = (Book) session.getAttribute("book"); %>
        <table>
            <tr>
                <th>Copy Barcode</th>
                <th>Title</th>
                <th>Author</th>
                <th>Status</th>
            </tr>
            <tr>
                <th><%= book.copyBarcode %></th>
                <td><%= book.title %></td>
                <td><%= book.author.replace("\n", "<br/>") %></td>
                <td><%= book.status %> <% if(book.status.equals("Checked Out")) { %>, Due <%= book.dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) %> <% } %></td>
            </tr>
        </table>
<% }
    else if(session.getAttribute("booklist") != null) {
        List<Book> booklist = (List<Book>) session.getAttribute("booklist"); %>
        <p><strong><%= booklist.size() %></strong> copies found.</p>
        <table>
            <tr>
                <th>Copy Barcode</th>
                <th>Title</th>
                <th>Author</th>
                <th>Status</th>
            </tr>
            <% for(Book book : booklist) { %>
                    <tr>
                        <th><%= book.copyBarcode %></th>
                        <td><%= book.title %></td>
                        <td><%= book.author.replace("\n", "<br/>") %></td>
                        <td><%= book.status %> <% if(book.status.equals("Checked Out")) { %>, Due <%= book.dueDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) %> <% } %></td>
                    </tr>
            <% } %>
        </table>
<%
    } %>
    <audio src="assets/sound/ding.mp3" style="display:none;" autoplay></audio>
<% } %>
<script>
    const searchBy = document.getElementById("searchBy");
    const copyBarcodeSearch = document.getElementById("copyBarcodeSearch");
    const titleBarcodeSearch = document.getElementById("titleBarcodeSearch");
    const criteriaSearch = document.getElementById("criteriaSearch");
    const inputCopyCode = document.getElementById("copyBarcode");
    const inputTitleCode = document.getElementById("titleBarcode");
    const inputCriteria = document.getElementById("criteria");

    searchBy.addEventListener("change", event => {
        switch (event.target.selectedIndex) {
            case 0:
                copyBarcodeSearch.style.display = "block"
                inputCopyCode.required = true;
                titleBarcodeSearch.style.display = "none";
                inputTitleCode.required = false;
                criteriaSearch.style.display = "none";
                inputCriteria.required = false;
                break;
            case 1:
                copyBarcodeSearch.style.display = "none"
                inputCopyCode.required = false;
                titleBarcodeSearch.style.display = "block";
                inputTitleCode.required = true;
                criteriaSearch.style.display = "none";
                inputCriteria.required = false;
                break;
            default:
                copyBarcodeSearch.style.display = "none"
                inputCopyCode.required = false;
                titleBarcodeSearch.style.display = "none";
                inputTitleCode.required = false
                criteriaSearch.style.display = "block";
                inputCriteria.required = true;
        }
    });
</script>
<% session.removeAttribute("status");
session.removeAttribute("criteria");%>
<%@ include file="assets/include/footer.jsp" %>