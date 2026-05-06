<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "catalog-search", pageTitle = "Catalog Search"; %>
<%@ include file="assets/include/header.jsp" %>
<%
    if(user == null) {
        session.setAttribute("status", SC_UNAUTHORIZED);
        response.sendRedirect("/library/signin.jsp");
    }
    else {
        int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");%>

        <h3>Catalog Search</h3>
        <form action="" method="POST">
            <div class="form-field">
                <label for="searchBy">Search By:</label>
                <select name="searchBy" id="searchBy">
                    <option value="copyBarcode">Copy Barcode (Exact Match)</option>
                    <option value="titleBarcode">Title Barcode (Exact Match)</option>
                    <option value="title">Title (Contains)</option>
                    <option value="author">Author Name (Contains)</option>
                </select>
            </div>
            <div class="form-field" id="copyBarcodeSearch">
                <label for="copyBarcode">Copy Barcode</label>
                <input type="text" name="copyBarcode" id="copyBarcode" required/>
            </div>
            <div class="form-field" id="titleBarcodeSearch" style="display: none;">
                <label for="titleBarcode">Title Barcode</label>
                <input type="text" name="titleBarcode" id="titleBarcode" required/>
            </div>
            <div class="form-field" id="criteriaSearch" style="display: none;">
                <label for="criteria">Search Criteria</label>
                <input type="text" name="criteria" id="criteria" required/>
            </div>
            <button type="submit" id="search-submit" name="search-submit">Search <img src="assets/img/proceed.png" alt="Search"/></button>
        </form>

<script>
    const searchBy = document.getElementById("searchBy");
    const copyBarcodeSearch = document.getElementById("copyBarcodeSearch");
    const titleBarcodeSearch = document.getElementById("titleBarcodeSearch");
    const criteriaSearch = document.getElementById("criteriaSearch");

    searchBy.addEventListener("change", event => {
        switch (event.target.selectedIndex) {
            case 0:
                copyBarcodeSearch.style.display = "block"
                titleBarcodeSearch.style.display = "none";
                criteriaSearch.style.display = "none";
                break;
            case 1:
                copyBarcodeSearch.style.display = "none"
                titleBarcodeSearch.style.display = "block";
                criteriaSearch.style.display = "none";
                break;
            default:
                copyBarcodeSearch.style.display = "none"
                titleBarcodeSearch.style.display = "none";
                criteriaSearch.style.display = "block";
        }
    });
</script>

<%@ include file="assets/include/footer.jsp" %>
<% } %>