<%@ page import="static javax.servlet.http.HttpServletResponse.*" %>
<% String pageName = "catalog-search", pageTitle = "Catalog Search"; %>
<%@ include file="assets/include/header.jsp" %>
<% int status = session.getAttribute("status") == null ? SC_OK : (int)session.getAttribute("status");
    if(status == SC_NOT_FOUND) { %>
        <p id="error"><img src="assets/img/cross.png" alt="Error"/><strong>Error</strong>: No copies found. Please double-check your search criteria and try again.</p>
        <audio src="assets/sound/bonk.mp3" style="display:none;" autoplay></audio>
    <% } else if(status == SC_ACCEPTED) { %>
        <audio src="assets/sound/ding.mp3" style="display:none;" autoplay></audio>
    <% } %>
        <h3>Catalog Search</h3>
        <form action="CatalogSearchServlet" method="POST">
            <div class="form-field">
                <label for="searchBy">Search By</label>
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
                <input type="text" name="titleBarcode" id="titleBarcode"/>
            </div>
            <div class="form-field" id="criteriaSearch" style="display: none;">
                <label for="criteria">Search Criteria</label>
                <input type="text" name="criteria" id="criteria"/>
            </div>
            <button type="submit" id="search-submit" name="search-submit">Search <img src="assets/img/proceed.png" alt="Search"/></button>
        </form>

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
<% session.removeAttribute("status"); %>
<%@ include file="assets/include/footer.jsp" %>